package com.mmtr.finance.parser.service.impl;

import com.mmtr.finance.parser.model.entity.User;
import com.mmtr.finance.parser.model.entity.UserPortfolioSummaries;
import com.mmtr.finance.parser.repository.UserPortfolioSummariesRepository;
import com.mmtr.finance.parser.service.UserPortfolioSummariesService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
@Log4j2
public class UserPortfolioSummariesServiceImpl implements UserPortfolioSummariesService {

    private final UserPortfolioSummariesRepository userPortfolioSummariesRepository;

    @Autowired
    public UserPortfolioSummariesServiceImpl(UserPortfolioSummariesRepository userPortfolioSummariesRepository) {
        this.userPortfolioSummariesRepository = userPortfolioSummariesRepository;
    }

    /**
     * Getting the current record on the user's portfolio
     *
     * @param userId
     * @return
     */
    @Override
    public Optional<UserPortfolioSummaries> findTopByUserOrderByIdDesc(long userId) {
        return userPortfolioSummariesRepository.findTopByUserOrderByIdDesc(userId);
    }

    @Override
    public void save(UserPortfolioSummaries userPortfolioSummaries) {
        userPortfolioSummariesRepository.save(userPortfolioSummaries);
    }

    /**
     * Updating statistics on the user's portfolio, or creating a new record
     *
     * @param user
     */
    @Override
    public void createOrUpdateUserPortfolioSummary(User user) {
        Date currentDate = new Date();
        Optional<UserPortfolioSummaries> optionalSummaries = userPortfolioSummariesRepository
                .findByUserAndDate(user, currentDate);

        if (optionalSummaries.isEmpty()) {
            userPortfolioSummariesRepository
                    .createUserPortfolioSummary(user.getId(),
                            new SimpleDateFormat("dd-MM-yyyy")
                                    .format(currentDate));
        } else {
            UserPortfolioSummaries userPortfolioSummaries = optionalSummaries.get();
            userPortfolioSummariesRepository
                    .updateUserPortfolioSummary(userPortfolioSummaries.getId(),
                            user.getId());
        }
    }
}
