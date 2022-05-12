package com.mmtr.finance.parser.service.impl;

import com.mmtr.finance.parser.message.MessageProvider;
import com.mmtr.finance.parser.service.EmailService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Component
@Log4j2
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;
    private final MessageProvider messageProvider;
    private final String host;

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender,
                            SpringTemplateEngine templateEngine,
                            MessageProvider messageProvider) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
        this.messageProvider = messageProvider;
        this.host = messageProvider.getMessage("host.url");
    }

    /**
     * Sending a password recovery email
     *
     * @param recipient
     * @param recoveryToken
     */
    @Override
    @Async
    public void sendRecoveryPassword(String recipient, String recoveryToken) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            mimeMessageHelper.setTo(recipient);
            mimeMessageHelper.setSubject(
                    messageProvider.getMessage("email.recoveryPasswordSubject"));

            //fill options in template
            String recoveryPasswordLink = String.format("%s%s%s", host,
                    messageProvider.getMessage("email.addressRecoveryPassword"),
                    recoveryToken);
            Context context = new Context();
            context.setVariable("link_recovery_password", recoveryPasswordLink);
            context.setVariable("host", host);

            //get template with properties
            String html = templateEngine.process("recovery-password-template", context);
            mimeMessageHelper.setText(html, true);
            emailSender.send(message);
        } catch (Exception ex) {
            log.error("Error send recovery password" + ex);
        }
    }
}
