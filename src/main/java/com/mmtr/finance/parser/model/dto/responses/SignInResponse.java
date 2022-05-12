package com.mmtr.finance.parser.model.dto.responses;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignInResponse {
    private String accessToken;

    private Long id;

    private String role;

    @ApiModelProperty(notes = "Сообщение об ошибке при авторизации")
    private String errorMessage;
}
