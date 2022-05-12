package com.mmtr.finance.parser.model.dto;

import com.mmtr.finance.parser.model.entity.User;
import com.mmtr.finance.parser.utils.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String login;

    @NotBlank
    private String password;

    public User fromUserDtoToUser(){
        User user = new User();
        user.setEmail(email);
        user.setLogin(login);
        user.setRole(Role.ROLE_USER);
        user.setRegistrationDate(new Date());
        return user;
    }
}
