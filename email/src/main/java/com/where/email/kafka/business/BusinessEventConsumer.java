package com.where.email.kafka.business;

import com.where.email.kafka.register.RegisterTokenEvent;
import com.where.email.service.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BusinessEventConsumer {
    private final EmailSender emailSender;

    @KafkaListener(topics = "business-status-update-topic")
    public void consumeRegisterConfirmationEvent(BusinessStatusUpdateEvent businessStatusUpdateEvent) {
        emailSender.sendBusinessStatusUpdatedEmail(businessStatusUpdateEvent);
    }
}
