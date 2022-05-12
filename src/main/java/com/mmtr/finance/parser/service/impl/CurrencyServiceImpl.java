package com.mmtr.finance.parser.service.impl;

import com.google.gson.Gson;
import com.mmtr.finance.parser.exception.CurrencyNotFound;
import com.mmtr.finance.parser.model.dto.open_exchange_rates.ExchangeRate;
import com.mmtr.finance.parser.model.dto.responses.CurrencyResponse;
import com.mmtr.finance.parser.model.entity.Currency;
import com.mmtr.finance.parser.repository.CurrencyRepository;
import com.mmtr.finance.parser.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Value("${openexchangerates.appId}")
    private String appId;

    @Autowired
    public CurrencyServiceImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    /**
     * Updating currency rates
     *
     * @param code
     * @return
     * @throws CurrencyNotFound
     */
    @Override
    public CurrencyResponse getExchangeRate(String code) throws CurrencyNotFound {
        Currency currency = currencyRepository
                .findByCodeIgnoreCase(code)
                .orElseThrow(CurrencyNotFound::new);

        return new CurrencyResponse(
                currency.getExchangeRate(), //exchangeRate
                currency.getTmstmp()); // updateDate
    }

    @Override
    public void updateCurrencyRate(String code) throws IOException {
        ExchangeRate exchangeRate = getActualExchangeRate(code.toUpperCase());
        Optional<Currency> currency = currencyRepository
                .findByCodeIgnoreCase(code);

        if (currency.isEmpty()) {
            Currency newCurrency = new Currency(code, //code
                    exchangeRate.getRates().getRUB(), //exchange rate
                    new Date()); //update date
            currencyRepository.save(newCurrency);
        } else {
            Currency currencyObject = currency.get();
            currencyObject.setExchangeRate(exchangeRate
                    .getRates()
                    .getRUB());
            currencyObject.setTmstmp(new Date());
            currencyRepository.save(currencyObject);
        }
    }

    /**
     * Getting the latest current exchange rate
     *
     * @param code
     * @return
     * @throws IOException
     */
    private ExchangeRate getActualExchangeRate(String code) throws IOException {
        String requestUrl = String
                .format("https://openexchangerates.org/api/latest.json?app_id=%s&base=%s&symbols=RUB",
                        appId,
                        code);

        InputStream inputStream = new URL(requestUrl).openStream();
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream,
                        StandardCharsets.UTF_8));
        String jsonText = readAll(bufferedReader);
        return new Gson().fromJson(jsonText, ExchangeRate.class);
    }

    private static String readAll(Reader reader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int cp;
        while ((cp = reader.read()) != -1) {
            stringBuilder.append((char) cp);
        }
        return stringBuilder.toString();
    }
}
