package com.mmtr.finance.parser.controller;

import com.mmtr.finance.parser.exception.CurrencyNotFound;
import com.mmtr.finance.parser.model.dto.responses.CurrencyResponse;
import com.mmtr.finance.parser.service.CurrencyService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/nsi")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(description = "Действия с справочниками", name = "nsi-controller")
public class NsiController {

    private final CurrencyService currencyService;

    @Autowired
    public NsiController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @ApiOperation(value = "Получение курса валюты и даты его обновления",
            authorizations = {@Authorization(value = "Bearer")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешное получение данных"),
            @ApiResponse(code = 404, message = "Запрашиваемые данные не были обнаружены"),
            @ApiResponse(code = 500, message = "Непредвиденная ошибка в ходе обработки")}
    )
    @RequestMapping(value = "/currency/{code}", method = RequestMethod.GET)
    public ResponseEntity<CurrencyResponse> getCurrencyExchangeRate(@PathVariable String code) {
        try {
            return ResponseEntity.ok(currencyService.getExchangeRate(code));
        } catch (CurrencyNotFound ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
