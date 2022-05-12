package com.mmtr.finance.parser.model.dto.responses;

import com.mmtr.finance.parser.model.entity.Company;
import com.mmtr.finance.parser.model.entity.Indicator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Model for displaying company information (company view page)
 */
@Getter
@Setter
@NoArgsConstructor
public class CompanyInfoResponse {

    public CompanyInfoResponse(String name,
                               String ticker,
                               String sector) {
        this.name = name;
        this.ticker = ticker;
        this.sector = sector;
    }

    @ApiModelProperty(notes = "Полное наименование компании")
    private String name;

    @ApiModelProperty(notes = "Тикер компании (уникальное сокращенное наименование)")
    private String ticker;

    @ApiModelProperty(notes = "Наименование сектора экономики, к которому относится компания")
    private String sector;

    @ApiModelProperty(notes = "Дата обновления по компании (выводить отдельно)")
    private Date date;

    @ApiModelProperty(notes = "Индекс, в который входит компания")
    private String indexCompany;

    @ApiModelProperty(notes = "Капитализация, млн $")
    private Float marketCap; //Millions

    @ApiModelProperty(notes = "Выручка, млн ")
    private Float income;  //Millions

    @ApiModelProperty(notes = "Продажи, млн $")
    private Float sales;  //Millions

    @ApiModelProperty(notes = "Балансовая стоимость на акцию")
    private Float bookPerShare;

    @ApiModelProperty(notes = "Денежная стоимость акции")
    private Float cashPerShare;

    @ApiModelProperty(notes = "Дивиденды на акцию в год, $")
    private Float dividend;

    @ApiModelProperty(notes = "Процент дивидендов, %")
    private Float dividendPercent;

    @ApiModelProperty(notes = "Количество сотрудников")
    private Integer employees;

    @ApiModelProperty(notes = "Рекомендация аналитиков (1-покупать, 5-продавать)")
    private Float recommendation;

    @ApiModelProperty(notes = "P/E (цена/прибыль")
    private Float priceToEarnings;

    @ApiModelProperty(notes = "Прогнозный P/E")
    private Float forwardPriceToEarnings;

    @ApiModelProperty(notes = "PEG (переоцененность цены на акцию)")
    private Float peg;

    @ApiModelProperty(notes = "P/S (цена/выручка)")
    private Float priceToSales;

    @ApiModelProperty(notes = "P/B (цена/балансовая стоимость)")
    private Float priceToBook;

    @ApiModelProperty(notes = "P/C (цена/денежный поток)")
    private Float priceToCashPerShare;

    @ApiModelProperty(notes = "P/FCF (Цена/свободный денежный поток)")
    private Float priceToFreeCashFlow;

    @ApiModelProperty(notes = "Quick ratio (коэффициент срочной ликвидности)")
    private Float quickRatio;

    @ApiModelProperty(notes = "Current ratio (коэффициент ликвидности")
    private Float currentRatio;

    @ApiModelProperty(notes = "Debt/Eq (заёмный капиал/собственный)")
    private Float debtToEquity;

    @ApiModelProperty(notes = "LT Debt/Eq (долгосрочная задолженность/собственный капитал)")
    private Float longTermDebtToEquity;

    @ApiModelProperty(notes = "SMA20 (скользящая средняя, 20 дней)")
    private Float twentyDaysSimpleMoving;

    @ApiModelProperty(notes = "Доля чистой прибыли владельца акции")
    private Float dilutedEps;

    @ApiModelProperty(notes = "Доходность акции на следующий год")
    private Float nextYearEstimateEps;

    @ApiModelProperty(notes = "Доходность акции на следующий квартал")
    private Float nextQuartalEstimateEps;

    @ApiModelProperty(notes = "Доходность акции текущий год")
    private Float thisYearEps;

    @ApiModelProperty(notes = "Доходность акции следующий год")
    private Float nextYearEps;

    @ApiModelProperty(notes = "Доходность акции следующие 5 лет")
    private Float nextFiveYearsEps;

    @ApiModelProperty(notes = "Доходность акции за последние 5 лет")
    private Float pastFiveYearsEps;

    @ApiModelProperty(notes = "Продажи за последние 5 лет")
    private Float salesPastFiveYears;

    @ApiModelProperty(notes = "Квартальный рост выручки")
    private Float quartalRevenueGrowth;

    @ApiModelProperty(notes = "Ежеквартальный рост прибыли")
    private Float quarterlyEarningsGrowth;

    @ApiModelProperty(notes = "SMA50")
    private Float fiftiethDaysSimpleMoving;

    @ApiModelProperty(notes = "Владение инсайдерами")
    private Float insiderOwn;

    @ApiModelProperty(notes = "Транзакции инсайдеров")
    private Float insiderTrans;

    @ApiModelProperty(notes = "Доля институциональных инвесторов")
    private Float instOwn;

    @ApiModelProperty(notes = "Транзакции институциональных инвесторов")
    private Float instTrans;

    @ApiModelProperty(notes = "Рентабельность активов")
    private Float roa;

    @ApiModelProperty(notes = "Рентабельность собственного капитала")
    private Float roe;

    @ApiModelProperty(notes = "Окупаемость инвестиций")
    private Float roi;

    @ApiModelProperty(notes = "Валовая прибыль")
    private Float grossMargin;

    @ApiModelProperty(notes = "Операционная рентабельность")
    private Float operatingMargin;

    @ApiModelProperty(notes = "Доходность")
    private Float profitMargin;

    @ApiModelProperty(notes = "Коэффициент выплаты дивидендов")
    private Float dividendPayoutRatio;

    @ApiModelProperty(notes = "SMA200")
    private Float twoHundredthDaysSimpleMoving;

    @ApiModelProperty(notes = "Целевая цена")
    private Float targetPrice;

    @ApiModelProperty(notes = "Изменение (неделя)")
    private Float performanceWeek;

    @ApiModelProperty(notes = "Изменение (месяц)")
    private Float performanceMonth;

    @ApiModelProperty(notes = "Изменение (квартал)")
    private Float performanceQuarter;

    @ApiModelProperty(notes = "Изменение (пол года)")
    private Float performanceHalfYear;

    @ApiModelProperty(notes = "Изменение (год)")
    private Float performanceYear;

    @ApiModelProperty(notes = "Изменение (с начала года)")
    private Float performanceYtd;

    @ApiModelProperty(notes = "Цена за одну акцию, $")
    private Float price;

    /**
     * Constructor for the case when indicators were found for the company
     *
     * @param company
     * @param indicator
     * @return
     */
    public CompanyInfoResponse getCompanyInfoResponse(Company company, Indicator indicator) {
        CompanyInfoResponse response = new CompanyInfoResponse(company.getName(),
                company.getTicker(),
                company.getSector());

        response.setDate(indicator.getDate());
        response.setIndexCompany(indicator.getIndexCompany());
        response.setMarketCap(indicator.getMarketCap());
        response.setIncome(indicator.getIncome());
        response.setSales(indicator.getSales());
        response.setBookPerShare(indicator.getBookPerShare());
        response.setCashPerShare(indicator.getCashPerShare());
        response.setDividend(indicator.getDividend());
        response.setDividendPercent(indicator.getDividendPercent());
        response.setEmployees(indicator.getEmployees());
        response.setRecommendation(indicator.getRecommendation());
        response.setPriceToEarnings(indicator.getPriceToEarnings());
        response.setForwardPriceToEarnings(indicator.getForwardPriceToEarnings());
        response.setPeg(indicator.getPeg());
        response.setPriceToSales(indicator.getPriceToSales());
        response.setPriceToBook(indicator.getPriceToBook());
        response.setPriceToCashPerShare(indicator.getPriceToCashPerShare());
        response.setPriceToFreeCashFlow(indicator.getPriceToFreeCashFlow());
        response.setQuickRatio(indicator.getQuickRatio());
        response.setCurrentRatio(indicator.getCurrentRatio());
        response.setDebtToEquity(indicator.getDebtToEquity());
        response.setLongTermDebtToEquity(indicator.getLongTermDebtToEquity());
        response.setTwentyDaysSimpleMoving(indicator.getTwentyDaysSimpleMoving());
        response.setDilutedEps(indicator.getDilutedEps());
        response.setNextYearEstimateEps(indicator.getNextYearEstimateEps());
        response.setNextQuartalEstimateEps(indicator.getNextQuartalEstimateEps());
        response.setThisYearEps(indicator.getThisYearEps());
        response.setNextYearEps(indicator.getNextYearEps());
        response.setNextFiveYearsEps(indicator.getNextFiveYearsEps());
        response.setPastFiveYearsEps(indicator.getPastFiveYearsEps());
        response.setQuartalRevenueGrowth(indicator.getQuartalRevenueGrowth());
        response.setQuarterlyEarningsGrowth(indicator.getQuarterlyEarningsGrowth());
        response.setFiftiethDaysSimpleMoving(indicator.getFiftiethDaysSimpleMoving());
        response.setInsiderOwn(indicator.getInsiderOwn());
        response.setInsiderTrans(indicator.getInsiderTrans());
        response.setInstOwn(indicator.getInstOwn());
        response.setInstTrans(indicator.getInstTrans());
        response.setRoa(indicator.getRoa());
        response.setRoe(indicator.getRoe());
        response.setRoi(indicator.getRoi());
        response.setGrossMargin(indicator.getGrossMargin());
        response.setOperatingMargin(indicator.getOperatingMargin());
        response.setProfitMargin(indicator.getProfitMargin());
        response.setDividendPayoutRatio(indicator.getDividendPayoutRatio());
        response.setTwoHundredthDaysSimpleMoving(indicator.getTwoHundredthDaysSimpleMoving());
        response.setTargetPrice(indicator.getTargetPrice());
        response.setPerformanceWeek(indicator.getPerformanceWeek());
        response.setPerformanceMonth(indicator.getPerformanceMonth());
        response.setPerformanceQuarter(indicator.getPerformanceQuarter());
        response.setPerformanceHalfYear(indicator.getPerformanceHalfYear());
        response.setPerformanceYear(indicator.getPerformanceYear());
        response.setPerformanceYtd(indicator.getPerformanceYtd());
        response.setPrice(indicator.getPrice());

        return response;
    }
}
