package com.mmtr.finance.parser.service;

import com.mmtr.finance.parser.model.dto.PortfolioDto;
import com.mmtr.finance.parser.model.dto.requests.TickerListToUserRequest;
import com.mmtr.finance.parser.model.dto.responses.*;
import com.mmtr.finance.parser.model.entity.Company;
import com.mmtr.finance.parser.model.entity.Portfolio;
import com.mmtr.finance.parser.model.entity.User;

import java.util.List;

public interface CompanyService {
    List<Company> findByIsActualTrue();

    List<Company> findBy();

    List<Portfolio> findByUser(User user);

    CompanyInfoResponse getCompanyInfo(long companyId);

    PagingResponse<CompanyResponse> getCompanies(int page, int size,
                                                 String name, String sector);

    PagingResponse<CompanyUserResponse> getCompaniesWithAuthUser(int page, int size, String login,
                                                                 String name, String sector);

    PagingResponse<CompanyUserResponse> getUserPortfolioCompanies(int page, int size, String login);

    MessageResponse addCompanyToUserPortfolio(PortfolioDto request, String login);

    MessageResponse addCompaniesToUserPortfolio(TickerListToUserRequest request, String login);

    MessageResponse changeCountStocksUserPortfolio(long userToCompanyId, PortfolioDto request);

    MessageResponse deleteCompanyFromPortfolio(Long id);

    void save(List<Company> companies);
}
