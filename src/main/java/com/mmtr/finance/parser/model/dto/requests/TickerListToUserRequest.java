package com.mmtr.finance.parser.model.dto.requests;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class TickerListToUserRequest {
    @ApiModelProperty(notes = "Список тикеров, которые ввел пользователь", required = true)
    @NotBlank
    private String tickers;
}
