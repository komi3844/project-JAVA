package com.mmtr.finance.parser.model.dto;

import com.mmtr.finance.parser.model.entity.Company;
import com.mmtr.finance.parser.model.entity.Portfolio;
import com.mmtr.finance.parser.model.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class PortfolioDto {
    @NotNull
    private Long companyId;

    @NotNull
    @ApiModelProperty(notes = "Количество акций компании")
    @Positive
    @Min(value = 1)
    @Max(value = 1_000_000)
    private Integer countStocks;

    public Portfolio fromPortfolioDtoToPortfolio(User user, Company company){
        Portfolio portfolio = new Portfolio();
        portfolio.setUser(user);
        portfolio.setCompany(company);
        portfolio.setCountStocks(countStocks);
        return portfolio;
    }
}
