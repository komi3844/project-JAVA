package com.mmtr.finance.parser.model.dto.responses;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyResponse {
    private Long id;

    @ApiModelProperty(notes = "Наименование компании")
    private String name;

    @ApiModelProperty(notes = "Краткое наименование компании на бирже")
    private String ticker;

    @ApiModelProperty(notes = "Наименование сектора экономики")
    private String sector;
}
