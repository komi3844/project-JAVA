package com.mmtr.finance.parser.security.service;

import com.mmtr.finance.parser.model.entity.User;
import com.mmtr.finance.parser.security.utils.JwtUserFactory;
import com.mmtr.finance.parser.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userService
                .findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User with username not found"));
        return JwtUserFactory.create(user);
    }
}
