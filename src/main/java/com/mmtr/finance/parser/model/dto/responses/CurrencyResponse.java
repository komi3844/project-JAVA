package com.mmtr.finance.parser.model.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyResponse {
    private Float exchangeRate;

    private Date updateDate;
}
