package com.mmtr.finance.parser.utils;

import lombok.AllArgsConstructor;

@AllArgsConstructor

public enum IndexExcelIndicator {
    marketCap(6),
    income(7),
    sales(8),
    bookPerShare(9),
    cashPerShare(10),
    dividend(11),
    dividendPercent(12),
    recommendation(14),

    priceToEarnings(15),
    forwardPriceToEarnings(16),
    peg(17),
    priceToSales(18),
    priceToBook(19),
    priceToCashPerShare(20),
    priceToFreeCashFlow(21),
    quickRatio(22),
    currentRatio(23),
    debtToEquity(24),
    longTermDebtToEquity(25),
    twentyDaysSimpleMoving(26),

    dilutedEps(27),
    nextYearEstimateEps(28),
    nextQuartalEstimateEps(29),
    thisYearEps(30),
    nextYearEps(31),
    nextFiveYearsEps(32),
    pastFiveYearsEps(33),
    salesPastFiveYears(34),
    quartalRevenueGrowth(35),
    quarterlyEarningsGrowth(36),
    fiftiethDaysSimpleMoving(37),

    insiderOwn(38),
    insiderTrans(39),
    instOwn(40),
    instTrans(41),
    roa(42),
    roe(43),
    roi(44),
    grossMargin(45),
    operatingMargin(46),
    profitMargin(47),
    dividendPayoutRatio(48),
    twoHundredthDaysSimpleMoving(49),
    targetPrice(50),

    performanceWeek(51),
    performanceMonth(52),
    performanceQuarter(53),
    performanceHalfYear(54),
    performanceYear(55),
    performanceYtd(56),
    price(57);

    private final Integer index;

    public Integer getIndex() {
        return index;
    }
}
