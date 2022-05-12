package com.mmtr.finance.parser.service;

import com.mmtr.finance.parser.model.entity.Company;
import com.mmtr.finance.parser.model.entity.Indicator;

import java.util.List;
import java.util.Optional;

public interface IndicatorService {
    Optional<Indicator> findTopByCompanyOrderByIdDesc(Company company);

    void deletePrevCompaniesIndicators();

    void save(List<Indicator> listIndicators);
}
