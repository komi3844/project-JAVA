package com.mmtr.finance.parser.service.impl;

import com.mmtr.finance.parser.exception.ExpiredRecoveryToken;
import com.mmtr.finance.parser.exception.NotFoundRecoveryToken;
import com.mmtr.finance.parser.model.entity.RecoveryPassword;
import com.mmtr.finance.parser.model.entity.User;
import com.mmtr.finance.parser.repository.RecoveryPasswordRepository;
import com.mmtr.finance.parser.service.RecoveryPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RecoveryPasswordServiceImpl implements RecoveryPasswordService {

    private final RecoveryPasswordRepository recoveryPasswordRepository;
    @Value("${recovery_password.expired}")
    private long validityInMs;

    @Autowired
    public RecoveryPasswordServiceImpl(RecoveryPasswordRepository recoveryPasswordRepository) {
        this.recoveryPasswordRepository = recoveryPasswordRepository;
    }

    /**
     * Obtaining password recovery record by temporary token,
     * date verification enabled
     *
     * @param token
     * @return
     * @throws NotFoundRecoveryToken
     * @throws ExpiredRecoveryToken
     */
    @Override
    public RecoveryPassword findByTokenAndCheckDate(String token)
            throws NotFoundRecoveryToken, ExpiredRecoveryToken {

        RecoveryPassword recoveryPassword = recoveryPasswordRepository
                .findByToken(token)
                .orElseThrow(NotFoundRecoveryToken::new);

        boolean validDateToken = !recoveryPassword
                .getExpirationDate()
                .before(new Date());

        if (!validDateToken) throw new ExpiredRecoveryToken();
        return recoveryPassword;
    }

    /**
     * Create a new password recovery entry
     *
     * @param user
     * @param token
     */
    @Override
    public void createRecoveryPassword(User user, String token) {
        RecoveryPassword recoveryPassword = new RecoveryPassword();
        recoveryPassword.setUser(user);
        recoveryPassword.setToken(token);
        recoveryPassword.setExpirationDate(new Date(new Date().getTime() + validityInMs));
        recoveryPasswordRepository.save(recoveryPassword);
    }

    /**
     * Deleting a specific password recovery entry
     * (after a successful password change)
     *
     * @param recoveryPassword
     */
    @Override
    public void deleteRecoveryPassword(RecoveryPassword recoveryPassword) {
        recoveryPasswordRepository.delete(recoveryPassword);
    }

    /**
     * Deleting previous password recovery entries
     *
     * @param user
     */
    @Override
    public void deletePrevRecoveryPasswords(User user) {
        List<RecoveryPassword> recoveryPasswordList = recoveryPasswordRepository.findByUser(user);
        recoveryPasswordList.forEach(recoveryPasswordRepository::delete);
    }
}
