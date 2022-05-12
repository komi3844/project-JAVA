package com.mmtr.finance.parser.model.dto.responses;

import com.mmtr.finance.parser.utils.Role;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class UserResponse {
    public UserResponse(long userId,
                        String email,
                        String login,
                        Date registrationDate,
                        Role role) {
        this.userId = userId;
        this.email = email;
        this.login = login;
        this.registrationDate = registrationDate;
        this.role = role;
    }

    private long userId;

    private String email;

    private String login;

    private Date registrationDate;

    private Role role;

    @ApiModelProperty(notes = "Общие показатели портфеля пользователя")
    private UserPortfolioSummaryResponse userPortfolioSummaries;
}
