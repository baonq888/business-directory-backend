package com.where.auth.kafka;

import com.where.auth.dto.request.AppUserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserProducer {
    private final KafkaTemplate<String, AppUserDTO> kafkaTemplate;
    public void sendOrderConfirmation(AppUserDTO appUserDTO) {
        log.info("Sending order confirmation");
        Message<AppUserDTO> message = MessageBuilder
                .withPayload(appUserDTO)
                .setHeader(KafkaHeaders.TOPIC,"user-topic")
                .build();
        kafkaTemplate.send(message);
    }
}
