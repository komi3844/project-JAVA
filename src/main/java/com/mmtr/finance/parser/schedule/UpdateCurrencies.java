package com.mmtr.finance.parser.schedule;

import com.mmtr.finance.parser.service.CurrencyService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Log4j2
@Getter
@Setter
public class UpdateCurrencies {

    private final CurrencyService currencyService;

    @Autowired
    public UpdateCurrencies(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Scheduled(cron = "0 0 * ? * *")

    public void updateCompaniesCurrencyRate() {
        try {
            currencyService.updateCurrencyRate("USD");
        } catch (Exception ex) {
            log.error("Error updates currency rate", ex);
        }
    }
}
