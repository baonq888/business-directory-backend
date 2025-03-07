package com.where.email.kafka.register;

import com.where.email.service.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegisterTokenConsumer {
    private final EmailSender emailSender;

    @KafkaListener(topics = "register-confirmation-token-topic")
    public void consumeRegisterConfirmationEvent(RegisterTokenEvent registerTokenEvent) {

    }
}
