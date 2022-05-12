package com.mmtr.finance.parser.model.dto.open_exchange_rates;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExchangeRate {
    private String disclaimer;

    private String license;

    private Long timestamp;

    private String base;

    private Rates rates;
}
