package com.where.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static com.where.email.service.EmailTemplates.REGISTER_CONFIRMATION;
import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailSenderImpl implements EmailSender {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    @Value("${email.from}")
    private String fromEmail;

    @Override
    @Async
    @Retryable(
            retryFor = MessagingException.class, // specify the exception to retry
            maxAttempts = 4, // default is 3
            backoff = @Backoff(delay = 3000) // set the backoff delay to 3 seconds
    )
    public void sendSignUpConfirmationEmail(String to, String name, String link) {

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            final String templateName = REGISTER_CONFIRMATION.getTemplate();

            Map<String, Object> variables = new HashMap<>();
            variables.put("name", name);
            variables.put("link", link);
            Context context = new Context();
            context.setVariables(variables);

            MimeMessageHelper messageHelper = new MimeMessageHelper(
                    mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    UTF_8.name()
            );
            messageHelper.setFrom(fromEmail);
            messageHelper.setSubject(REGISTER_CONFIRMATION.getSubject());

            String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);
            messageHelper.setTo(to);

            mailSender.send(mimeMessage);

            log.info("INFO - Register Confirmation Email successfully sent to {} with template {} ", to, templateName);
        } catch (MessagingException e) {
            log.error("Fail to send email",e);
            throw new IllegalStateException("Failed to send email");
        }
    }


    public String handleMessagingException(MessagingException e) {
        return "";
    }
}
