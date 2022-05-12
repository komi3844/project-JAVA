package com.mmtr.finance.parser.schedule;

import com.mmtr.finance.parser.service.FinvizParseService;
import com.mmtr.finance.parser.service.IndicatorService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;

@Service
@Log4j2
@Getter
@Setter
public class UpdateCompaniesIndicators {

    private final FinvizParseService finvizParseService;
    private final IndicatorService indicatorService;

    @Autowired
    public UpdateCompaniesIndicators(FinvizParseService finvizParseService, IndicatorService indicatorService) {
        this.finvizParseService = finvizParseService;
        this.indicatorService = indicatorService;
    }

    @PostConstruct
    @Scheduled(cron = "0 0 2 * * ?")
    public void updateCompaniesIndicatorData() {
        try {
            finvizParseService.updatesCompaniesIndicators();
            indicatorService.deletePrevCompaniesIndicators();
            log.info(String.format("Success updates company indicator data. Date: %s",
                    new Date()));
        } catch (Exception ex) {
            log.error("Error with scheduling task - updateCompaniesIndicatorData", ex);
        }
    }
}
