package com.mmtr.finance.parser.schedule;

import com.mmtr.finance.parser.service.FinvizParseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Log4j2
public class UpdateCompaniesData {

    private final FinvizParseService finvizParseService;

    @Scheduled(cron = "0 0 3 1 * ?")
    public void updateCompaniesData() {
        try {
            finvizParseService.updatesCompanies();
            log.info(String.format("Success updates company data. Date: %s",
                    new Date()));
        } catch (IOException e) {
            log.error("Error with scheduling task - updateCompaniesData", e);
        }

    }
}
