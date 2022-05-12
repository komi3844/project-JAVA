package com.mmtr.finance.parser.security.utils;

import com.mmtr.finance.parser.model.entity.User;
import com.mmtr.finance.parser.security.model.JwtUser;
import com.mmtr.finance.parser.utils.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class JwtUserFactory {
    public JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        var cb = mapToGrantedAuthorities(
                Collections.singletonList(
                        user.getRole()));
        return new JwtUser(
                user.getLogin(),
                user.getPassword(),
                cb);
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Role> userRoles) {
        return userRoles.stream()
                .map(role -> new SimpleGrantedAuthority(role.toString()))
                .collect(Collectors.toList());
    }
}
