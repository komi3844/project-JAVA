package com.mmtr.finance.parser.service;

import java.io.IOException;

public interface FinvizParseService {
    void updatesCompaniesIndicators() throws IOException;

    void updatesCompanies() throws IOException, NumberFormatException;
}
