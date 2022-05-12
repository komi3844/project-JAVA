package com.mmtr.finance.parser.model.entity;

import com.mmtr.finance.parser.model.dto.responses.CompanyResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "companies")
public class Company {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ApiModelProperty(notes = "Тикер компании (уникальное сокращенное наименование)", required = true)
    @Column(name = "ticker")
    private String ticker;

    @ApiModelProperty(notes = "Наименование сектора экономики, к которому относится компания", required = true)
    @Column(name = "sector")
    private String sector;

    @Column(name = "is_actual")
    private Boolean isActual;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<Portfolio> userToCompanies;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<Indicator> indicators;

    public CompanyResponse getCompanyResponse() {
        return new CompanyResponse(id, name, ticker, sector);
    }
}
