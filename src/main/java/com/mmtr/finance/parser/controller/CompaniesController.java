package com.mmtr.finance.parser.controller;

import com.mmtr.finance.parser.model.dto.PortfolioDto;
import com.mmtr.finance.parser.model.dto.requests.TickerListToUserRequest;
import com.mmtr.finance.parser.model.dto.responses.*;
import com.mmtr.finance.parser.security.model.JwtUser;
import com.mmtr.finance.parser.service.CompanyService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/companies")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(description = "Операции с компаниями", name = "companies-controller")
@RequiredArgsConstructor
public class CompaniesController{
    private final CompanyService companyService;

    @ApiOperation(value = "Получение информации по компании (включая все мультипликаторы, цену акции и т.д.)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное получение данных"),
            @ApiResponse(code = 401, message = "Необходимо авторизоваться"),
            @ApiResponse(code = 403, message = "Доступ запрещен (только для пользователей системы)"),
            @ApiResponse(code = 404, message = "Компании не обнаружено"),
            @ApiResponse(code = 500, message = "Непредвиденная ошибка в ходе обработки")}
    )
    @PreAuthorize("hasAuthority('ROLE_USER') || hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = "{companyId}")
    public ResponseEntity<CompanyInfoResponse> getCompanyInfo(@PathVariable long companyId) {
        CompanyInfoResponse response = companyService.getCompanyInfo(companyId);
        return response != null ?
                ResponseEntity.ok(response) :
                ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Получение списка компаний (просмотр из личного кабинета, с учетом добавленных)",
            authorizations = {@Authorization(value = "Bearer")})
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseEntity<PagingResponse<CompanyUserResponse>> getCompaniesWithAuthUser(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sector) {

        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(companyService
                .getCompaniesWithAuthUser(page,
                        size,
                        ((JwtUser) principal).getUsername(),
                        name,
                        sector));
    }

    @ApiOperation(value = "Получение списка добавленных компаний пользоватем (портфеля) ",
            authorizations = {@Authorization(value = "Bearer")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное получение данных"),
            @ApiResponse(code = 401, message = "Необходимо авторизоваться"),
            @ApiResponse(code = 403, message = "Доступ запрещен (только для пользователей системы)"),
            @ApiResponse(code = 500, message = "Непредвиденная ошибка в ходе обработки")}
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @RequestMapping(value = "/portfolio", method = RequestMethod.GET)
    public ResponseEntity<PagingResponse<CompanyUserResponse>> getUserPortfolioCompanies(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(companyService
                .getUserPortfolioCompanies(page,
                        size,
                        ((JwtUser) principal).getUsername()));
    }

    @ApiOperation(value = "Получение списка компаний (просмотр не авторизованного пользователя)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное получение данных"),
            @ApiResponse(code = 500, message = "Непредвиденная ошибка в ходе обработки")}
    )
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<PagingResponse<CompanyResponse>> getCompanies(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sector) {

        return ResponseEntity.ok(companyService
                .getCompanies(page,
                        size,
                        name,
                        sector));
    }


    @ApiOperation(value = "Добавление компании в портфолио",
            authorizations = {@Authorization(value = "Bearer")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное выполнение операции, если любая из ошибок, " +
                    "в том числе и непредвиденная - сообщение об ошибке"),
            @ApiResponse(code = 401, message = "Необходимо авторизоваться"),
            @ApiResponse(code = 403, message = "Доступ запрещен (только для пользователей системы)")}
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @RequestMapping(value = "/add-company-to-user", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<MessageResponse> addCompanyToUserPortfolio(@Valid @RequestBody PortfolioDto request) {
        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(companyService
                .addCompanyToUserPortfolio(request,
                        ((JwtUser) principal).getUsername()));
    }

    @ApiOperation(value = "Добавление списка компаний в портфолио (всегда добавляется только одна акция)",
            authorizations = {@Authorization(value = "Bearer")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное выполнение операции, если любая из ошибок, " +
                    "в том числе и непредвиденная - сообщение об ошибке"),
            @ApiResponse(code = 401, message = "Необходимо авторизоваться"),
            @ApiResponse(code = 403, message = "Доступ запрещен (только для пользователей системы)")}
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @RequestMapping(value = "/add-companies-to-user", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<MessageResponse> addCompaniesToUserPortfolio(@Valid @RequestBody TickerListToUserRequest request) {
        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(companyService
                .addCompaniesToUserPortfolio(request,
                        ((JwtUser) principal).getUsername()));
    }


    @ApiOperation(value = "Изменение количества акций по компании",
            authorizations = {@Authorization(value = "Bearer")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное выполнение операции, если любая из ошибок, " +
                    "в том числе и непредвиденная - сообщение об ошибке"),
            @ApiResponse(code = 401, message = "Необходимо авторизоваться"),
            @ApiResponse(code = 403, message = "Доступ запрещен (только для пользователей системы)")}
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @RequestMapping(value = "/change-count-stocks/{userToCompanyId}", method = RequestMethod.PATCH, consumes = "application/json")
    public ResponseEntity<MessageResponse> changeCountStocksUserPortfolio(@PathVariable long userToCompanyId,
                                                                          @Valid @RequestBody PortfolioDto request) {
        return ResponseEntity.ok(companyService.changeCountStocksUserPortfolio(userToCompanyId, request));
    }

    @ApiOperation(value = "Удаление компании из портфеля пользователя",
            authorizations = {@Authorization(value = "Bearer")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное выполнение операции"),
            @ApiResponse(code = 401, message = "Необходимо авторизоваться"),
            @ApiResponse(code = 403, message = "Доступ запрещен (только для пользователей системы)"),
            @ApiResponse(code = 500, message = "Непредвиденная ошибка")}
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @RequestMapping(value = "/remove-company-from-user/{userToCompanyId}", method = RequestMethod.DELETE)
    public ResponseEntity<MessageResponse> deleteCompanyFromPortfolio(@PathVariable Long userToCompanyId) {
        return ResponseEntity.ok(companyService.deleteCompanyFromPortfolio(userToCompanyId));
    }
}
