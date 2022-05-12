package com.mmtr.finance.parser.model.dto.requests;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ChangePasswordRequest {
    @NotBlank
    @ApiModelProperty(notes = "Токен для изменения пароля (в адресной строке)", required = true)
    private String token;

    @NotBlank
    @ApiModelProperty(notes = "Новый пароль пользователя", required = true)
    private String password;
}
