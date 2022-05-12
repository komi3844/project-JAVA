package com.mmtr.finance.parser.security.configuration;

import com.mmtr.finance.parser.security.filter.TokenFilter;
import com.mmtr.finance.parser.security.service.TokenProvider;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class TokenConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final TokenProvider tokenProvider;

    public TokenConfigurer(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void configure(HttpSecurity httpSecurity) {
        TokenFilter tokenFilter = new TokenFilter(tokenProvider);
        httpSecurity.addFilterBefore(tokenFilter,
                UsernamePasswordAuthenticationFilter.class);
    }
}
