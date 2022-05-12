package com.mmtr.finance.parser.config;

import com.mmtr.finance.parser.exception.UnauthorizedEntryPoint;
import com.mmtr.finance.parser.security.configuration.TokenConfigurer;
import com.mmtr.finance.parser.security.service.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
@Configuration
@EnableAsync
@EnableScheduling
@EnableGlobalMethodSecurity(
        prePostEnabled = true)
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter {
    private final TokenProvider tokenProvider;
    private final UnauthorizedEntryPoint unauthorizedEntryPoint;

    @Autowired
    public WebSecurityConfig(TokenProvider tokenProvider,
                             UnauthorizedEntryPoint unauthorizedEntryPoint) {
        this.tokenProvider = tokenProvider;
        this.unauthorizedEntryPoint = unauthorizedEntryPoint;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/api/v1/users/sign-in",
                        "/api/v1/users/sign-up",
                        "/api/v1/users/recovery-password",
                        "/api/v1/users/change-password",
                        "/api/v1/companies",
                        "/api/v1/users/test", //test
                        "/api/v1/nsi/test", //test
                        "/swagger-resources/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v2/api-docs",
                        "/webjars/**").permitAll()
                .anyRequest().authenticated().and()
                .apply(new TokenConfigurer(tokenProvider)).and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint);
    }
}
