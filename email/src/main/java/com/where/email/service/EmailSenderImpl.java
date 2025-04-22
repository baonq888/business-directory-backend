package com.where.email.service;

import com.where.email.kafka.business.BusinessStatusUpdateEvent;
import com.where.email.kafka.register.RegisterTokenEvent;
import com.where.email.service.helper.EmailSenderHelper;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import static com.where.email.service.EmailTemplates.BUSINESS_STATUS_UPDATED;
import static com.where.email.service.EmailTemplates.REGISTER_CONFIRMATION;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailSenderImpl implements EmailSender {
    private final EmailSenderHelper emailSenderHelper;

    @Override
    @Async
    @Retryable(
            retryFor = MessagingException.class,
            maxAttempts = 4,
            backoff = @Backoff(delay = 3000)
    )
    public void sendSignUpConfirmationEmail(RegisterTokenEvent registerTokenEvent) {

        try {
            String to = registerTokenEvent.getEmail();
            String name = registerTokenEvent.getName();
            String link = registerTokenEvent.getLink();
            final String templateName = REGISTER_CONFIRMATION.getTemplate();

            Map<String, Object> variables = new HashMap<>();
            variables.put("name", name);
            variables.put("link", link);
            emailSenderHelper.sendEmail(
                    to, variables, templateName, REGISTER_CONFIRMATION.getSubject()
            );

            log.info("INFO - Register Confirmation Email successfully sent to {}", to);
        } catch (MessagingException e) {
            log.error("Fail to send email",e);
            throw new IllegalStateException("Failed to send email");
        }
    }

    @Override
    @Async
    @Retryable(
            retryFor = MessagingException.class,
            maxAttempts = 4,
            backoff = @Backoff(delay = 3000)
    )
    public void sendBusinessStatusUpdatedEmail(BusinessStatusUpdateEvent businessStatusUpdateEvent) {
        try {
            String to = businessStatusUpdateEvent.getBusinessOwnerEmail();
            String businessName = businessStatusUpdateEvent.getBusinessName();
            String newStatus = businessStatusUpdateEvent.getStatus();
            final String templateName = BUSINESS_STATUS_UPDATED.getTemplate();

            Map<String, Object> variables = new HashMap<>();
            variables.put("businessName", businessName);
            variables.put("newStatus", newStatus);

            emailSenderHelper.sendEmail(
                    to, variables, templateName,BUSINESS_STATUS_UPDATED.getSubject()
            );

            log.info("INFO - Business Status Updated Email successfully sent to {}", to);
        } catch (MessagingException e) {
            log.error("Fail to send email",e);
            throw new IllegalStateException("Failed to send email");
        }
    }


    public String handleMessagingException(MessagingException e) {
        return e.getMessage();
    }
}
