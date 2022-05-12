package com.mmtr.finance.parser.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "indicators")
public class Indicator {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "index_company")
    private String indexCompany;

    @Column(name = "market_cap")
    private Float marketCap; //Millions

    @Column(name = "income")
    private Float income;  //Millions

    @Column(name = "sales")
    private Float sales;  //Millions

    @Column(name = "book_per_share")
    private Float bookPerShare;

    @Column(name = "cash_per_share")
    private Float cashPerShare;

    @Column(name = "dividend")
    private Float dividend;

    @Column(name = "dividend_percent")
    private Float dividendPercent;

    @Column(name = "employees")
    private Integer employees;

    @Column(name = "recommendation")
    private Float recommendation;

    @Column(name = "price_to_earnings")
    private Float priceToEarnings;

    @Column(name = "forward_price_to_earnings")
    private Float forwardPriceToEarnings;

    @Column(name = "peg")
    private Float peg;

    @Column(name = "price_to_sales")
    private Float priceToSales;

    @Column(name = "price_to_book")
    private Float priceToBook;

    @Column(name = "price_to_cash_per_share")
    private Float priceToCashPerShare;

    @Column(name = "price_to_free_cash_flow")
    private Float priceToFreeCashFlow;

    @Column(name = "quick_ratio")
    private Float quickRatio;

    @Column(name = "current_ratio")
    private Float currentRatio;

    @Column(name = "debt_to_equity")
    private Float debtToEquity;

    @Column(name = "long_term_debt_to_equity")
    private Float longTermDebtToEquity;

    @Column(name = "twenty_days_simple_moving")
    private Float twentyDaysSimpleMoving;

    @Column(name = "diluted_eps")
    private Float dilutedEps;

    @Column(name = "next_year_estimate_eps")
    private Float nextYearEstimateEps;

    @Column(name = "next_quartal_estimate_eps")
    private Float nextQuartalEstimateEps;

    @Column(name = "this_year_eps")
    private Float thisYearEps;

    @Column(name = "next_year_eps")
    private Float nextYearEps;

    @Column(name = "next_five_years_eps")
    private Float nextFiveYearsEps;

    @Column(name = "past_five_years_eps")
    private Float pastFiveYearsEps;

    @Column(name = "sales_past_five_years")
    private Float salesPastFiveYears;

    @Column(name = "quartal_revenue_growth")
    private Float quartalRevenueGrowth;

    @Column(name = "quarterlyEarningsGrowth")
    private Float quarterlyEarningsGrowth;

    @Column(name = "fiftieth_days_simple_moving")
    private Float fiftiethDaysSimpleMoving;

    @Column(name = "insider_own")
    private Float insiderOwn;

    @Column(name = "insider_trans")
    private Float insiderTrans;

    @Column(name = "inst_own")
    private Float instOwn;

    @Column(name = "instTrans")
    private Float instTrans;

    @Column(name = "roa")
    private Float roa;

    @Column(name = "roe")
    private Float roe;

    @Column(name = "roi")
    private Float roi;

    @Column(name = "gross_margin")
    private Float grossMargin;

    @Column(name = "operating_margin")
    private Float operatingMargin;

    @Column(name = "profit_margin")
    private Float profitMargin;

    @Column(name = "dividend_payout_ratio")
    private Float dividendPayoutRatio;

    @Column(name = "two_hundredth_days_simple_moving")
    private Float twoHundredthDaysSimpleMoving;

    @Column(name = "target_price")
    private Float targetPrice;

    @Column(name = "performance_week")
    private Float performanceWeek;

    @Column(name = "performance_month")
    private Float performanceMonth;

    @Column(name = "performance_quarter")
    private Float performanceQuarter;

    @Column(name = "performance_half_year")
    private Float performanceHalfYear;

    @Column(name = "performance_year")
    private Float performanceYear;

    @Column(name = "performance_ytd")
    private Float performanceYtd;

    @Column(name = "price")
    private Float price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;
}
