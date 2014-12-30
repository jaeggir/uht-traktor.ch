package ch.uhttraktor.website.rest.servlet.config;

import ch.uhttraktor.website.AppConstants;
import ch.uhttraktor.website.rest.security.AjaxAuthenticationFailureHandler;
import ch.uhttraktor.website.rest.security.AjaxAuthenticationSuccessHandler;
import ch.uhttraktor.website.rest.security.Http401UnauthorizedEntryPoint;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import static ch.uhttraktor.website.domain.SecurityRole.ANONYMOUS;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter implements EnvironmentAware {

    private Environment env;

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/**");
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

    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        if (env.acceptsProfiles(AppConstants.STAGE_DEV)) {
            auth.inMemoryAuthentication()
                .withUser("user").password("user").roles("USER", "NEWS").and()
                .withUser("admin").password("admin").roles("NEWS", "USER", "ADMIN");
        } else {
            auth.jdbcAuthentication().passwordEncoder(passwordEncoder())
                .usersByUsernameQuery("SELECT login, password, enabled FROM t_user WHERE login = ?")
                .authoritiesByUsernameQuery("SELECT u.login, a.authority FROM t_user u, t_user_authority a " +
                    "WHERE u.uuid = t.user_uuid AND u.login = ?");
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // enabled anonymous access, anonymous user has role ROLE_ANONYMOUS
                .anonymous().authorities(ANONYMOUS).and()
                .exceptionHandling()
                    .authenticationEntryPoint(new Http401UnauthorizedEntryPoint())
                    .and()
                .formLogin()
                    .loginProcessingUrl("/api/identity/login")
                    .successHandler(new AjaxAuthenticationSuccessHandler())
                    .failureHandler(new AjaxAuthenticationFailureHandler())
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .permitAll()
                    .and()
                .logout()
                    .logoutUrl("/api/identity/logout")
                    .deleteCookies("JSESSIONID")
                    .permitAll()
                    .and()
                .csrf() // see: http://docs.spring.io/spring-security/site/docs/3.2.5.RELEASE/reference/htmlsingle/#csrf
                    .disable()
                .authorizeRequests()
                     // deny all access by default - you can override that with @Secured annotations in the controllers
                    .antMatchers("/api/**").denyAll();
    }
}
