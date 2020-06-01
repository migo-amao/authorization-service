package wei.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

/**
 *  When providing our own AuthorizationServerConfigurerAdapter, it will override the one provided by the auto
 *  configuration.
 *
 *  The AuthorizationServerSecurityConfiguration will call the AuthorizationServerConfigurerAdapter:configure,
 *  so overriding this configure method will give your a chance to further configure the authorization of the
 *  the endpoints like "/oauth/token_key" or "/oauth/check_token", since these two endpoints are configured as
 *  denyAll() by default. Another way to override the default for these endpoints is via application.yml:
 *
 *     security.oauth2.authorization.token-key-access: permitAll()
 *     security.oauth2.authorization.check-token-access: isAuthenticated()
 *
 *  The AuthorizationServerSecurityConfiguration will call the AuthorizationServerConfigurerAdapter:configure,
 *  so overriding this configure method will give your a chance to further configure the ClientDetailsService.
 *
 *  AuthorizationServerEndpointsConfiguration will call AuthorizationServerConfigurerAdapter:configure,
 *  so overriding this configure method will give your a chance to further configure the endpoints.
 *  AuthorizationServerEndpointsConfiguration also expose the endpoints as beans.
 *
 *  To configure the authorization server to return the token as JWT format, just need to expose two beans:
 *
 *        JwtAccessTokenConverter that converts the access token to a JWT format
 *        TokenStore that can store the JWT token
 *
 *  For the password and authorization_code grants, we need the authentication manager that internally uses
 *  DaoAuthenticationProvider to authenticate the user.
 */

@Configuration
public class JwtAuthServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public JwtAuthServerConfiguration(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        //this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                //.passwordEncoder(new BCryptPasswordEncoder(4));
                .passwordEncoder(this.passwordEncoder);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(this.dataSource);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(tokenStore())
                .accessTokenConverter(accessTokenConverter())
                .authenticationManager(this.authenticationManager);
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() throws Exception {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        accessTokenConverter.setKeyPair(keyPair());
        return accessTokenConverter;
    }

    @Bean
    public TokenStore tokenStore() throws Exception {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public KeyPair keyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    /*@Bean("clientPasswordEncoder")
    public PasswordEncoder clientPasswordEncoder() {
        return new BCryptPasswordEncoder(4);
    }*/
}
