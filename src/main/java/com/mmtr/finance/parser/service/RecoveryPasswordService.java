package com.mmtr.finance.parser.service;

import com.mmtr.finance.parser.exception.ExpiredRecoveryToken;
import com.mmtr.finance.parser.exception.NotFoundRecoveryToken;
import com.mmtr.finance.parser.model.entity.RecoveryPassword;
import com.mmtr.finance.parser.model.entity.User;

public interface RecoveryPasswordService {
    RecoveryPassword findByTokenAndCheckDate(String token) throws NotFoundRecoveryToken, ExpiredRecoveryToken;

    void createRecoveryPassword(User user, String token);

    void deleteRecoveryPassword(RecoveryPassword recoveryPassword);

    void deletePrevRecoveryPasswords(User user);
}
