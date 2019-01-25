package wei.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * The resource server is on the same app as auth server to provide the authentication object via access token. It will
 * install a {@link org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter} to validate
 * the access token. If the token is good, it allows the work flow to /user/me endpoint to return the authentication.
 */
@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.antMatcher("/user/me").authorizeRequests().anyRequest().authenticated();
    }
}
