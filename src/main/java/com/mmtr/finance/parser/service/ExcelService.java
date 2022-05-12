package com.mmtr.finance.parser.service;

import com.mmtr.finance.parser.model.entity.Portfolio;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ExcelService {
    File getExcelFileUserCompaniesIndicator(List<Portfolio> userToCompanies) throws IOException,
            NoSuchFieldException,
            IllegalAccessException;
}
