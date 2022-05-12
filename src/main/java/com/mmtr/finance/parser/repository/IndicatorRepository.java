package com.mmtr.finance.parser.repository;

import com.mmtr.finance.parser.model.entity.Company;
import com.mmtr.finance.parser.model.entity.Indicator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface IndicatorRepository extends JpaRepository<Indicator, Long> {
    Optional<Indicator> findTopByCompanyOrderByIdDesc(Company company);

    @Modifying
    @Query(value = "DELETE " +
            "FROM fin_focus.indicators " +
            "WHERE ID NOT IN (SELECT MAX(ID) " +
            "FROM fin_focus.indicators " +
            "GROUP BY company_id)", nativeQuery = true)
    @Transactional
    void deletePrevCompaniesIndicators();
}
