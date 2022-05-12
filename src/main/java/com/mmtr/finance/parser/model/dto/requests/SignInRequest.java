package com.mmtr.finance.parser.model.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {
    @NotNull
    private String login;

    @NotNull
    private String password;
}
