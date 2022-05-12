package com.mmtr.finance.parser.service;

import com.mmtr.finance.parser.model.entity.User;
import com.mmtr.finance.parser.model.entity.UserPortfolioSummaries;

import java.util.Optional;

public interface UserPortfolioSummariesService {
    Optional<UserPortfolioSummaries> findTopByUserOrderByIdDesc(long userId);

    void save(UserPortfolioSummaries userPortfolioSummaries);

    void createOrUpdateUserPortfolioSummary(User user);
}
