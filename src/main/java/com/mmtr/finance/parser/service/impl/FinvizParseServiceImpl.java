package com.mmtr.finance.parser.service.impl;

import com.mmtr.finance.parser.model.entity.Company;
import com.mmtr.finance.parser.model.entity.Indicator;
import com.mmtr.finance.parser.service.CompanyService;
import com.mmtr.finance.parser.service.FinvizParseService;
import com.mmtr.finance.parser.service.IndicatorService;
import com.mmtr.finance.parser.utils.ConvertIndicator;
import com.mmtr.finance.parser.utils.IndexParseIndicator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
@Log4j2
public class FinvizParseServiceImpl implements FinvizParseService {

    @Value("${finviz.url_company}")
    private String urlCompany;

    @Value("${finviz.url_screener}")
    private String urlScreener;

    @Value("${finviz.total_elements_selector}")
    private String totalElementsSelector;

    @Value("${finviz.companies.selector}")
    private String companiesSelector;

    @Value("${finviz.cssSelector}")
    private String cssSelector;

    private final CompanyService companyService;
    private final IndicatorService indicatorService;

    @Override
    public void updatesCompaniesIndicators() {
        List<Indicator> indicators = new ArrayList<>();
        List<Company> companyList = companyService.findByIsActualTrue();
        companyList.forEach(company -> {
            try {
                indicators.add(createTodayIndicator(company));
            } catch (Exception ex) {
                log.error("Error with creating a new company indicator record", ex);
            }
        });
        indicatorService.save(indicators);
    }

    @Override
    public void updatesCompanies() throws NumberFormatException {
        try {
            List<Company> companyList = companyService.findBy();
            Document doc = Jsoup.connect(urlScreener).get();
            Elements elements = doc.select(totalElementsSelector);
            String textElement = elements.get(0)
                    .childNodes().get(1)
                    .childNodes().get(0)
                    .toString();
            int countPages = Integer.parseInt(
                    textElement.substring(
                            textElement.indexOf("/") + 1));
            saveCompaniesFromPage(companyList, urlScreener);
            if (countPages >= 2) {
                for (int i = 1; i < countPages; i++) {
                    String urlPageScreener = String.format("%s&r=%d", urlScreener, (i * 20) + 1);
                    saveCompaniesFromPage(companyList, urlPageScreener);
                }
            }
        } catch (Exception ex) {
            log.error("Error update companies", ex);
        }
    }

    private Indicator createTodayIndicator(Company company)
            throws IOException, IllegalAccessException, NoSuchFieldException {
        String url = urlCompany + company.getTicker();
        Indicator indicator = new Indicator();
        indicator.setCompany(company);
        indicator.setDate(new Date());

        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.select(cssSelector);

        int maxCountElements = 72;
        if (elements.size() == maxCountElements) {
            indicator.setIndexCompany(elements.get(0).text());
            indicator.setEmployees(ConvertIndicator
                    .convertToInteger(elements.get(48).text()));

            for (IndexParseIndicator floatIndicator : IndexParseIndicator.values()) {
                Field name = indicator.getClass().getDeclaredField(floatIndicator.toString());

                Float value = ConvertIndicator.convertToFloat(
                        elements.get(floatIndicator.getSelectorIndex()).text());

                name.setAccessible(true);
                name.set(indicator, value);
                name.setAccessible(false);
            }
        } else {
            log.error(String.format("Not correct count elements finviz parse. Count: %d", elements.size()));
        }

        return indicator;
    }

    private void saveCompaniesFromPage(List<Company> currentCompaniesList,
                                       String urlPageScreener) throws IOException {
        Document page = Jsoup.connect(urlPageScreener).get();
        List<Company> companiesFromPage = new ArrayList<>(getNotAddedCompanies(page,
                currentCompaniesList));
        companyService.save(companiesFromPage);
    }

    private List<Company> getNotAddedCompanies(Document page, List<Company> currentCompaniesList) {
        List<Company> companyList = new ArrayList<>();
        int countRows = 21;

        for (int rowNumber = 2; rowNumber <= countRows; rowNumber++) {
            String elementSelector = getElementSelector(companiesSelector, rowNumber);
            Elements tickerElement = page.select(elementSelector);
            if (tickerElement.isEmpty()) break;
            String ticker = tickerElement.get(0).text();
            if (!companyIsAlreadyAdded(currentCompaniesList, ticker)) {
                Company company = new Company();
                company.setTicker(ticker);
                company.setIsActual(true);
                companyList.add(company);
            }
        }
        return companyList;
    }

    private static String getElementSelector(String selector, int row) {
        return selector.replace("ROW", row + "");
    }

    private static boolean companyIsAlreadyAdded(List<Company> currentCompaniesList, String ticker) {
        return currentCompaniesList.stream()
                .anyMatch(company -> company.getTicker().equals(ticker));
    }
}
