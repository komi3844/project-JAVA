package com.mmtr.finance.parser.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    @Async
    void sendRecoveryPassword(String recipient, String recoveryToken);
}
