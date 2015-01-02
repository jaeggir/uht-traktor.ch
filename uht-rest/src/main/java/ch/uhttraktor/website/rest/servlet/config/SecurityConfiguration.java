package ch.uhttraktor.website.rest.servlet.config;

import ch.uhttraktor.website.AppConstants;
import ch.uhttraktor.website.rest.security.Http401UnauthorizedEntryPoint;
import ch.uhttraktor.website.rest.security.InMemoryTokenManager;
import ch.uhttraktor.website.rest.security.TokenAuthenticationFilter;
import ch.uhttraktor.website.rest.security.TokenAuthenticationService;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static ch.uhttraktor.website.domain.SecurityRole.ANONYMOUS_ROLE_SHORT;
import static ch.uhttraktor.website.domain.SecurityRole.ANONYMOUS_USER_NAME;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter implements EnvironmentAware {

    private Environment env;

    public SecurityConfiguration() {
        super(true); // disable defaults
    }

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new StandardPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        if (env.acceptsProfiles(AppConstants.STAGE_DEV)) {
            auth.inMemoryAuthentication()
                    .withUser(ANONYMOUS_USER_NAME).password(ANONYMOUS_USER_NAME).roles(ANONYMOUS_ROLE_SHORT).and()
                    .withUser("user").password("user").roles("USER", "NEWS").and()
                    .withUser("admin").password("admin").roles("NEWS", "USER", "ADMIN");
        } else {
            auth.jdbcAuthentication().passwordEncoder(passwordEncoder())
                    .usersByUsernameQuery("SELECT login, password, enabled FROM t_user WHERE login = ?")
                    .authoritiesByUsernameQuery("SELECT u.login, a.authority FROM t_user u, t_user_authority a " +
                            "WHERE u.uuid = t.user_uuid AND u.login = ?");
        }
    }

    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }

    @Bean
    public TokenAuthenticationService tokenAuthenticationService() throws Exception {
        return new TokenAuthenticationService(authenticationManagerBean(), new InMemoryTokenManager());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // disable anonymous access - however there is a user anonymous with password "anonymous" and authority
                // "ROLE_ANONYMOUS" which can be used to access some resources (e.g. GET /api/news).
                .anonymous().disable()
                // define default authentication entry point which throws a 401
                .exceptionHandling().authenticationEntryPoint(new Http401UnauthorizedEntryPoint()).and()
                // disable formLogin and logout - we have a token-based authentication
                .formLogin().disable().logout().disable()
                // token handling
                .addFilterBefore(new TokenAuthenticationFilter(
                        tokenAuthenticationService(),
                        "/api/identity/login",
                        "/api/identity/logout"
                ), UsernamePasswordAuthenticationFilter.class)
                // disable sessions - we use custom tokens instead
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // CSRF, see: http://docs.spring.io/spring-security/site/docs/3.2.5.RELEASE/reference/htmlsingle/#csrf
                .csrf().disable()
                // deny all access by default - you can override that with @Secured annotations in the controllers
                .authorizeRequests().antMatchers("/api/**").authenticated();
    }
}
