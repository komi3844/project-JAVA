package com.mmtr.finance.parser.schedule;

import com.mmtr.finance.parser.model.entity.User;
import com.mmtr.finance.parser.service.UserPortfolioSummariesService;
import com.mmtr.finance.parser.service.UserService;
import com.mmtr.finance.parser.utils.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Log4j2
@Getter
@Setter
public class UpdatePortfoliosSummary {

    private final UserService userService;
    private final UserPortfolioSummariesService userPortfolioSummariesService;

    @Autowired
    public UpdatePortfoliosSummary(UserService userService, UserPortfolioSummariesService userPortfolioSummariesService) {
        this.userService = userService;
        this.userPortfolioSummariesService = userPortfolioSummariesService;
    }

    @Scheduled(cron = "0 0 6 * * ?")
    public void updateUsersSummaryPortfolio() {
        List<User> userList = userService.findAllUsers(Role.ROLE_USER);
        for (User user : userList) {
            try {
                userPortfolioSummariesService
                        .createOrUpdateUserPortfolioSummary(user);
            } catch (Exception ex) {
                log.error(String.format("Error update user portfolio. userId: %d",
                        user.getId()), ex);
            }
        }
        log.info(String.format("Success updates users summary portfolio %s",
                new Date()));
    }
}
