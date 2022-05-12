package com.mmtr.finance.parser.model.dto.responses;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponse {
    @ApiModelProperty(notes = "Сообщение об ошибке")
    private String errorMessage;

    public MessageResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
