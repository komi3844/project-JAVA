package com.mmtr.finance.parser.utils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum IndexParseIndicator {
    marketCap(6),
    income(12),
    sales(18),
    bookPerShare(24),
    cashPerShare(30),
    dividend(36),
    dividendPercent(42),
    //employees(48), //Integer
    recommendation(66),

    priceToEarnings(1),
    forwardPriceToEarnings(7),
    peg(13),
    priceToSales(19),
    priceToBook(25),
    priceToCashPerShare(31),
    priceToFreeCashFlow(37),
    quickRatio(43),
    currentRatio(49),
    debtToEquity(55),
    longTermDebtToEquity(61),
    twentyDaysSimpleMoving(67),

    dilutedEps(2),
    nextYearEstimateEps(8),
    nextQuartalEstimateEps(14),
    thisYearEps(20),
    nextYearEps(26),
    nextFiveYearsEps(32),
    pastFiveYearsEps(38),
    salesPastFiveYears(44),
    quartalRevenueGrowth(50),
    quarterlyEarningsGrowth(56),
    fiftiethDaysSimpleMoving(68),

    insiderOwn(3),
    insiderTrans(9),
    instOwn(15),
    instTrans(21),
    roa(27),
    roe(33),
    roi(39),
    grossMargin(45),
    operatingMargin(51),
    profitMargin(57),
    dividendPayoutRatio(63),
    twoHundredthDaysSimpleMoving(69),
    targetPrice(28),

    performanceWeek(5),
    performanceMonth(11),
    performanceQuarter(17),
    performanceHalfYear(23),
    performanceYear(29),
    performanceYtd(35),
    price(65);

    private final Integer selectorIndex;

    public Integer getSelectorIndex() {
        return selectorIndex;
    }
}
