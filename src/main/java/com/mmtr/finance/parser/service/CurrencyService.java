package com.mmtr.finance.parser.service;

import com.mmtr.finance.parser.exception.CurrencyNotFound;
import com.mmtr.finance.parser.model.dto.responses.CurrencyResponse;

import java.io.IOException;

public interface CurrencyService {
    CurrencyResponse getExchangeRate(String code) throws CurrencyNotFound;

    void updateCurrencyRate(String code) throws IOException;
}