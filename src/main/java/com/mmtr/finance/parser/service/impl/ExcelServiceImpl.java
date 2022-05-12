package com.mmtr.finance.parser.service.impl;

import com.mmtr.finance.parser.model.entity.Company;
import com.mmtr.finance.parser.model.entity.Indicator;
import com.mmtr.finance.parser.model.entity.Portfolio;
import com.mmtr.finance.parser.service.ExcelService;
import com.mmtr.finance.parser.service.IndicatorService;
import com.mmtr.finance.parser.utils.IndexExcelIndicator;
import com.mmtr.finance.parser.utils.MapFromArrays;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ExcelServiceImpl implements ExcelService {

    private final IndicatorService indicatorService;

    @Autowired
    public ExcelServiceImpl(IndicatorService indicatorService) {
        this.indicatorService = indicatorService;
    }

    @Override
    public File getExcelFileUserCompaniesIndicator(List<Portfolio> userToCompanies) throws IOException, NoSuchFieldException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        String fileName = "Portfolio";
        Sheet sheet = workbook.createSheet(String.format("%s %s",
                fileName,
                new SimpleDateFormat("dd-MM-yyyy")
                        .format(new Date())));

        CellStyle headerStyle = getHeaderStyle(workbook);
        Map<Integer, String> columnsMap = getMapFields();

        Row header = sheet.createRow(0);
        for (Map.Entry<Integer, String> entry : columnsMap.entrySet()) {
            sheet.setColumnWidth(entry.getKey(), 6000);
            Cell headerCell = header.createCell(entry.getKey());
            headerCell.setCellValue(entry.getValue());
            headerCell.setCellStyle(headerStyle);
        }

        for (int i = 0; i < userToCompanies.size(); i++) {
            Row row = sheet.createRow(i + 1);
            Portfolio portfolio = userToCompanies.get(i);
            Company company = portfolio.getCompany();
            Indicator indicator = indicatorService
                    .findTopByCompanyOrderByIdDesc(company)
                    .orElse(null);

            if (indicator != null) {
                for (Map.Entry<Integer, String> entry : columnsMap.entrySet()) {
                    int currentKey = entry.getKey();
                    Cell rowCell = row.createCell(currentKey);
                    switch (currentKey) {
                        case 0:
                            rowCell.setCellValue(company.getName());
                            break;
                        case 1:
                            rowCell.setCellValue(company.getTicker());
                            break;
                        case 2:
                            rowCell.setCellValue(company.getSector());
                            break;
                        case 3:
                            rowCell.setCellValue(portfolio.getCountStocks());
                            break;
                        case 4:
                            Float price = indicator.getPrice();
                            if (price != null) {
                                //count cannot be null
                                rowCell.setCellValue(price * portfolio.getCountStocks());
                            } else {
                                rowCell.setCellValue("");
                            }
                            break;
                        case 5:
                            String valueIndexCompany = indicator.getIndexCompany();
                            valueIndexCompany = valueIndexCompany == null ? "" : valueIndexCompany;
                            rowCell.setCellValue(valueIndexCompany);
                            break;
                        case 13:
                            Integer countEmployees = indicator.getEmployees();
                            countEmployees = countEmployees == null ? 0 : countEmployees;
                            rowCell.setCellValue(countEmployees);
                            break;
                        default:
                            //for double values indicator
                            for (IndexExcelIndicator excelIndicator : IndexExcelIndicator.values()) {
                                if (currentKey == excelIndicator.getIndex()) {
                                    Field field = indicator.getClass()
                                            .getDeclaredField(excelIndicator.toString());
                                    try {
                                        field.setAccessible(true);
                                        rowCell.setCellValue((Double) field.get(indicator));
                                        field.setAccessible(false);
                                    } catch (Exception ex) {
                                        rowCell.setCellValue("");
                                    }
                                }
                            }
                            break;
                    }
                }
            }
        }

        int firstRow = 0, firstCol = 0;
        int lastRow = userToCompanies.size() + 1;
        int lastCol = getMapFields().size();
        sheet.setAutoFilter(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));

        File file = new File(String.format("%s.xls", fileName));
        FileOutputStream out = new FileOutputStream(file);
        workbook.write(out);
        workbook.close();
        return file;
    }

    private CellStyle getHeaderStyle(XSSFWorkbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        //set font
        XSSFFont font = workbook.createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        headerStyle.setFont(font);

        return headerStyle;
    }

    private static Map<Integer, String> getMapFields() {
        return MapFromArrays.convert(
                new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
                        10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
                        20, 21, 22, 23, 24, 25, 26, 27, 28, 29,
                        30, 31, 32, 33, 34, 35, 36, 37, 38, 39,
                        40, 41, 42, 43, 44, 45, 46, 47, 48, 49,
                        50, 51, 52, 53, 54, 55, 56, 57},
                new String[]{"Компания",
                        "Тикер",
                        "Сектор",
                        "Количество акций",
                        "Суммарная стоимость, $",
                        "Индекс",
                        "Капитализация, млн $",
                        "Выручка, млн $",
                        "Продажи, млн $",
                        "Балансовая стоимость на акцию",
                        "Денежная стоимость акции",
                        "Дивиденды на акцию в год, $",
                        "Процент дивидендов, %",
                        "Количество сотрудников",
                        "Рекомендация аналитиков (1-покупать, 5-продавать)",
                        "P/E",
                        "Forward P/E",
                        "PEG",
                        "P/S",
                        "P/B",
                        "P/C",
                        "P/FCF",
                        "Quick ratio",
                        "Current ratio",
                        "Debt/Eq ",
                        "LT Debt/Eq",
                        "SMA20",
                        "EPS (ttm)",
                        "EPS next Y",
                        "EPS next Q",
                        "EPS this Y",
                        "EPS next Y",
                        "EPS next 5Y",
                        "EPS past 5Y",
                        "Sales past 5Y",
                        "Sales Q/Q",
                        "EPS Q/Q",
                        "SMA50",
                        "Insider Own",
                        "Insider Trans",
                        "Inst Own",
                        "Inst Trans",
                        "ROA",
                        "ROE",
                        "ROI",
                        "Gross Margin",
                        "Oper. Margin",
                        "Profit Margin",
                        "Payout",
                        "SMA200",
                        "Target Price",
                        "Perf Week",
                        "Perf Month",
                        "Perf Quarter",
                        "Perf Half Y",
                        "Perf Year",
                        "Perf YTD",
                        "Price"});
    }
}
