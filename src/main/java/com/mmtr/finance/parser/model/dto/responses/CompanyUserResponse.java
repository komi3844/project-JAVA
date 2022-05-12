package com.mmtr.finance.parser.model.dto.responses;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CompanyUserResponse {
    @ApiModelProperty(notes = "Идентификатор записи")
    private Long companyPortfolioId;

    @ApiModelProperty(notes = "Количество акций")
    private Integer count;

    @ApiModelProperty(notes = "Идентификатор компании")
    private Long companyId;

    @ApiModelProperty(notes = "Наименование компании")
    private String name;

    @ApiModelProperty(notes = "Краткое наименование компании на бирже")
    private String ticker;

    @ApiModelProperty(notes = "Наименование сектора экономики")
    private String sector;
}
