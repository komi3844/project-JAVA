package com.mmtr.finance.parser.repository;

import com.mmtr.finance.parser.model.dto.responses.CompanyUserResponse;
import com.mmtr.finance.parser.model.entity.Company;
import com.mmtr.finance.parser.model.entity.Portfolio;
import com.mmtr.finance.parser.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    Optional<Portfolio> findByUserAndCompany(User user, Company company);

    @Query("SELECT uc " +
            "FROM Portfolio uc " +
            "JOIN Company c on c.id = uc.company.id " +
            "WHERE c.isActual = true")
    List<Portfolio> findByUser(User user);

    @Query("SELECT new com.mmtr.finance.parser.model.dto.responses.CompanyUserResponse(coalesce(uc.id,0), " +
            "coalesce(uc.countStocks,0), " +
            "c.id, " +
            "c.name, " +
            "c.ticker, " +
            "c.sector) " +
            "FROM Portfolio uc " +
            "JOIN Company c on c.id = uc.company.id " +
            "WHERE uc.user = :user " +
            "AND c.isActual = true " +
            "order by c.id asc")
    Page<CompanyUserResponse> findActualUserCompaniesPageable(User user, Pageable pageable);

    @Query("SELECT new com.mmtr.finance.parser.model.dto.responses.CompanyUserResponse(coalesce(uc.id,0), " +
            "coalesce(uc.countStocks,0), " +
            "c.id, " +
            "c.name, " +
            "c.ticker, " +
            "c.sector) " +
            "FROM Company c " +
            "LEFT JOIN Portfolio uc on c.id = uc.company.id " +
            "and uc.user = :user " +
            "WHERE (c.name LIKE %:name% " +
            "OR c.ticker LIKE %:name%) " +
            "AND c.sector LIKE %:sector% " +
            "AND c.isActual = true " +
            "order by c.id asc")
    Page<CompanyUserResponse> findActualCompaniesWithUserPageable(User user,
                                                                  String name,
                                                                  String sector,
                                                                  Pageable pageable);
}
