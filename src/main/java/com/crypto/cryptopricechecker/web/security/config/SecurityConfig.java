package com.crypto.cryptopricechecker.web.security.config;


import com.crypto.cryptopricechecker.service.UserService;
import com.crypto.cryptopricechecker.web.security.AuthHeaderProvider;
import com.crypto.cryptopricechecker.web.security.ContextHeaderPreAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
// TODO: find how its done in the newer Spring versions since {@link WebSecurityConfigurerAdapter} is deprecated
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()  //No Http Basic Login
                .csrf().disable()  //No CSRF token
                .formLogin().disable()  //No Form Login
                .logout().disable()  //No Logout
                //No Session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(new ContextHeaderPreAuthFilter(authenticationManager(),
                        userService),
                RequestHeaderAuthenticationFilter.class)
                .authorizeRequests()
                    .requestMatchers(new AntPathRequestMatcher("/**")).authenticated()
                    .anyRequest().denyAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authTokenHeaderAuthProvider());
    }

    @Bean
    public AuthenticationProvider authTokenHeaderAuthProvider() {
        return new AuthHeaderProvider();
    }

    @Bean
    public ContextHeaderPreAuthFilter authHeaderProvider() throws Exception {
        return new ContextHeaderPreAuthFilter(authenticationManager(), userService);
    }

}
