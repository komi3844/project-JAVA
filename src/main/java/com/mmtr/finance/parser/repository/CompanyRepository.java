package com.mmtr.finance.parser.repository;

import com.mmtr.finance.parser.model.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    @Query("SELECT c " +
            "FROM Company c " +
            "WHERE (c.name LIKE %:name% " +
            "OR c.ticker LIKE %:name%) " +
            "AND c.sector LIKE %:sector% " +
            "AND c.isActual = true " +
            "order by c.id asc")
    Page<Company> findAllCompanies(Pageable pageable, String name, String sector);

    @Query(value = "SELECT * " +
            "FROM fin_focus.companies " +
            "WHERE ID IN (SELECT company_id " +
            "FROM fin_focus.portfolios " +
            "WHERE user_id = :userId)", nativeQuery = true)
    List<Company> findUserPortfolioCompanies(@Param("userId") long userId);

    List<Company> findBy();

    List<Company> findByIsActualTrue();

    Optional<Company> findByTickerAndIsActualTrue(String ticker);
}
