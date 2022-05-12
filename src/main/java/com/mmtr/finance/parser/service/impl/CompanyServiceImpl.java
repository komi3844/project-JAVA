package com.mmtr.finance.parser.service.impl;

import com.mmtr.finance.parser.exception.CompanyAddedEarlier;
import com.mmtr.finance.parser.message.MessageProvider;
import com.mmtr.finance.parser.model.dto.PortfolioDto;
import com.mmtr.finance.parser.model.dto.requests.TickerListToUserRequest;
import com.mmtr.finance.parser.model.dto.responses.*;
import com.mmtr.finance.parser.model.entity.Company;
import com.mmtr.finance.parser.model.entity.Indicator;
import com.mmtr.finance.parser.model.entity.Portfolio;
import com.mmtr.finance.parser.model.entity.User;
import com.mmtr.finance.parser.repository.CompanyRepository;
import com.mmtr.finance.parser.repository.IndicatorRepository;
import com.mmtr.finance.parser.repository.PortfolioRepository;
import com.mmtr.finance.parser.service.CompanyService;
import com.mmtr.finance.parser.service.UserPortfolioSummariesService;
import com.mmtr.finance.parser.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final PortfolioRepository portfolioRepository;
    private final IndicatorRepository indicatorRepository;
    private final UserPortfolioSummariesService userPortfolioSummariesService;
    private final UserService userService;
    private final MessageProvider messageProvider;

    @Override
    public List<Company> findByIsActualTrue() {
        return companyRepository.findByIsActualTrue();
    }

    @Override
    public List<Company> findBy() {
        return companyRepository.findBy();
    }

    @Override
    public List<Portfolio> findByUser(User user) {
        return portfolioRepository.findByUser(user);
    }

    @Override
    public CompanyInfoResponse getCompanyInfo(long companyId) {
        CompanyInfoResponse companyInfoResponse = new CompanyInfoResponse();
        Optional<Company> companyOptional = companyRepository.findById(companyId);
        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();
            Optional<Indicator> indicatorOptional = indicatorRepository
                    .findTopByCompanyOrderByIdDesc(company);
            if (indicatorOptional.isPresent()) {
                Indicator indicator = indicatorOptional.get();
                companyInfoResponse = companyInfoResponse
                        .getCompanyInfoResponse(company, indicator);
            } else {
                companyInfoResponse = new CompanyInfoResponse(company.getName(),
                        company.getTicker(),
                        company.getSector());
            }
        }

        return !companyInfoResponse.equals(new CompanyInfoResponse()) ?
                companyInfoResponse :
                null;
    }

    @Override
    public PagingResponse<CompanyResponse> getCompanies(int page, int size,
                                                        String name, String sector) {
        Pageable paging = PageRequest.of(page - 1, size);
        name = name == null ? "" : name;
        sector = sector == null ? "" : sector;

        Page<Company> companyPage = companyRepository.findAllCompanies(paging, name, sector);
        List<CompanyResponse> companyResponseList = companyPage.getContent()
                .stream()
                .map(Company::getCompanyResponse)
                .collect(Collectors.toList());

        return new PagingResponse<>(companyResponseList,
                companyPage.getNumber(),
                companyPage.getTotalElements(),
                companyPage.getTotalPages());
    }

    @Override
    public PagingResponse<CompanyUserResponse> getCompaniesWithAuthUser(int page,
                                                                        int size,
                                                                        String login,
                                                                        String name,
                                                                        String sector) {
        Pageable paging = PageRequest.of(page - 1, size);
        name = name == null ? "" : name;
        sector = sector == null ? "" : sector;

        User user = userService
                .findByLogin(login)
                .orElse(null);

        Page<CompanyUserResponse> userToCompanies = portfolioRepository
                .findActualCompaniesWithUserPageable(user,
                        name,
                        sector,
                        paging);

        return new PagingResponse<>(userToCompanies.getContent(),
                userToCompanies.getNumber(),
                userToCompanies.getTotalElements(),
                userToCompanies.getTotalPages());
    }


    @Override
    public PagingResponse<CompanyUserResponse> getUserPortfolioCompanies(int page, int size, String login) {
        Pageable paging = PageRequest.of(page - 1, size);
        User user = userService
                .findByLogin(login)
                .orElse(null);

        Page<CompanyUserResponse> userPortfolioCompanies = portfolioRepository
                .findActualUserCompaniesPageable(user, paging);

        return new PagingResponse<>(userPortfolioCompanies.getContent(),
                userPortfolioCompanies.getNumber(),
                userPortfolioCompanies.getTotalElements(),
                userPortfolioCompanies.getTotalPages());
    }

    @Override
    public MessageResponse addCompanyToUserPortfolio(PortfolioDto request, String login) {
        MessageResponse messageResponse = new MessageResponse("");

        try {
            User user = userService.findByLogin(login)
                    .orElseThrow(Exception::new);
            Company company = companyRepository.findById(request.getCompanyId())
                    .orElseThrow(Exception::new);

            Portfolio portfolio = portfolioRepository
                    .findByUserAndCompany(user, company)
                    .orElse(null);
            if (portfolio != null) throw new CompanyAddedEarlier();

            Portfolio userCompany = request
                    .fromPortfolioDtoToPortfolio(user, company);
            portfolioRepository.save(userCompany);

            userPortfolioSummariesService.createOrUpdateUserPortfolioSummary(user);
            messageResponse.setErrorMessage("");

        } catch (CompanyAddedEarlier ex) {
            messageResponse.setErrorMessage(
                    messageProvider.getMessage("exception.companyAddedEarlier"));
        } catch (Exception ex) {
            messageResponse.setErrorMessage(
                    messageProvider.getMessage("reply.unexpectedError"));
        }

        return messageResponse;
    }

    @Override
    public MessageResponse addCompaniesToUserPortfolio(TickerListToUserRequest request, String login) {
        MessageResponse messageResponse = new MessageResponse("");
        try {
            userService.findByLogin(login).ifPresent(user -> {
                long userId = user.getId();
                List<String> tickerList = new ArrayList<>();
                Matcher matcher = Pattern.compile("[A-Z]{1,4}").matcher(request.getTickers().trim().toUpperCase());
                while (matcher.find()) {
                    tickerList.add(matcher.group());
                }
                if (!tickerList.isEmpty()) {
                    List<Company> currentUserCompanies = companyRepository.findUserPortfolioCompanies(userId);
                    List<Portfolio> resultCompaniesToAdd = new ArrayList<>();

                    for (String ticker : tickerList) {
                        companyRepository.findByTickerAndIsActualTrue(ticker).ifPresent(company -> {
                            if (currentUserCompanies.isEmpty()
                                    || !companyIsAlreadyAdded(currentUserCompanies, company)) {
                                Portfolio portfolio = new Portfolio();
                                portfolio.setCountStocks(1);
                                portfolio.setUser(user);
                                portfolio.setCompany(company);
                                resultCompaniesToAdd.add(portfolio);
                            }
                        });
                    }
                    if (!resultCompaniesToAdd.isEmpty()) {
                        portfolioRepository.saveAll(resultCompaniesToAdd);
                    } else {
                        messageResponse.setErrorMessage(
                                messageProvider.getMessage("reply.companiesNotFound"));
                    }

                } else {
                    messageResponse.setErrorMessage(
                            messageProvider.getMessage("reply.companiesNotFound"));
                }
            });
        } catch (Exception ex) {
            log.error("Unexpected error with adding list companies to user", ex);
            messageResponse.setErrorMessage(
                    messageProvider.getMessage("reply.unexpectedError"));
        }

        return messageResponse;
    }

    @Override
    public MessageResponse changeCountStocksUserPortfolio(long userToCompanyId, PortfolioDto request) {
        MessageResponse messageResponse = new MessageResponse(
                messageProvider.getMessage("reply.unexpectedError"));
        try {
            portfolioRepository.findById(userToCompanyId)
                    .ifPresent(userToCompany -> {
                        userToCompany.setCountStocks(request.getCountStocks());
                        portfolioRepository.save(userToCompany);
                        userPortfolioSummariesService
                                .createOrUpdateUserPortfolioSummary(userToCompany.getUser());
                        messageResponse.setErrorMessage("");
                    });
        } catch (Exception ex) {
            log.error("Unexpected error with edit user portfolio", ex);
        }
        return messageResponse;
    }

    @Override
    public MessageResponse deleteCompanyFromPortfolio(Long id) {
        Optional<Portfolio> portfolio = portfolioRepository.findById(id);
        if (portfolio.isPresent()) {
            Portfolio portfolioCompany = portfolio.get();
            User user = portfolioCompany.getUser();
            portfolioRepository.delete(portfolioCompany);
            userPortfolioSummariesService.createOrUpdateUserPortfolioSummary(user);
        }
        return new MessageResponse("");
    }

    @Override
    public void save(List<Company> companies) {
        companyRepository.saveAll(companies);
    }

    private boolean companyIsAlreadyAdded(List<Company> currentUserCompanies, Company newCompany) {
        return currentUserCompanies
                .stream()
                .anyMatch(company -> company.equals(newCompany));
    }
}