package com.mmtr.finance.parser.service.impl;

import com.mmtr.finance.parser.model.entity.Company;
import com.mmtr.finance.parser.model.entity.Indicator;
import com.mmtr.finance.parser.repository.IndicatorRepository;
import com.mmtr.finance.parser.service.IndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IndicatorServiceImpl implements IndicatorService {

    private final IndicatorRepository indicatorRepository;

    @Autowired
    public IndicatorServiceImpl(IndicatorRepository indicatorRepository) {
        this.indicatorRepository = indicatorRepository;
    }

    /**
     * Obtaining the latest current record of indicators for the company
     *
     * @param company
     * @return
     */
    @Override
    public Optional<Indicator> findTopByCompanyOrderByIdDesc(Company company) {
        return indicatorRepository.findTopByCompanyOrderByIdDesc(company);
    }

    /**
     * Removal of previously added indicators
     * (if you want the historical data in the future this method can be removed)
     */
    @Override
    public void deletePrevCompaniesIndicators() {
        indicatorRepository.deletePrevCompaniesIndicators();
    }

    @Override
    public void save(List<Indicator> listIndicators) {
        indicatorRepository.saveAll(listIndicators);
    }
}
