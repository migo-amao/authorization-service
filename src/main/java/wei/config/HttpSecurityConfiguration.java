package wei.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

/**
 * WebSecurityConfiguration has a bean defined as name "springSecurityFilterChain". This bean will build a web security
 * and call WebSecurityConfigurerAdapter:init to pass the web security. WebSecurityConfigurerAdapter:init further build
 * a http security and call WebSecurityConfigurerAdapter:configure on the http security.
 *
 * AuthorizationServerSecurityConfiguration is a subclass of WebSecurityConfigurerAdapter. Therefore it will be bootstrapped
 * by WebSecurityConfiguration. AuthorizationServerSecurityConfiguration during bootstrap will call any AuthorizationServerConfigurerAdapter
 * to configure the authorization of the endpoints /oauth/token_key, /oauth/check_token, etc. Therefore, there will be
 * one SpringSecurityFilterChain installed for the path ['/oauth/token', '/oauth/token_key', '/oauth/check_token'].
 *
 * In order to create another SpringSecurityFilterChain to handle the normal login, we need to provide this WebSecurityConfigurerAdpater.
 * Spring will install another SpringSecurityFilterChain to handle all requests other than the above token endpoints.
 * And this filter chain, by default, will install the UsernamePasswordAuthenticationFilter to handle the login for path
 * /login, which will be needed for the authorization_code grant type.
 */

@Configuration
@EnableWebSecurity
public class HttpSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .antMatchers("/h2/**", "/h2-console/**", "/.well-known/jwks.json")
                .permitAll()
                .anyRequest()
                .authenticated();
    }

    @Override
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Once expose either UserDetailsService bean or AuthenticationManager bean, Spring will not setup build-in default user and there is only one
     * authentication manager exposed.
     */
    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) throws Exception {
        //return new InMemoryUserDetailsManager(User.withUsername("user").password("{noop}pass").roles("USER").build());
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        manager.setAuthenticationManager(authenticationManagerBean());
        manager.setEnableAuthorities(false);
        manager.setEnableGroups(true);
        return manager;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

    /*@Bean
    public JwtAccessTokenConverter accessTokenConverter() throws Exception {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        accessTokenConverter.setKeyPair(keyPair());
        return accessTokenConverter;
    }

    @Bean
    public TokenStore tokenStore() throws Exception {
        return new JwtTokenStore(accessTokenConverter());
    }

    private KeyPair keyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }*/
}
