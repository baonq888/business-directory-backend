package com.where.business.kafka;

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
public class BusinessProducer {
    private final KafkaTemplate<String, BusinessEvent> kafkaTemplate;
    private final KafkaTemplate<String, BusinessEvent> kafkaTemplateStatusUpdate;
    private final KafkaTemplate<String, BusinessEvent> kafkaTemplateUpdate;

    public void sendBusinessCreateEvent(BusinessEvent businessEvent) {
        log.info("Sending business create message");
        Message<BusinessEvent> message = MessageBuilder
                .withPayload(businessEvent)
                .setHeader(KafkaHeaders.TOPIC,"business-create-topic")
                .build();
        kafkaTemplate.send(message);
    }
    public void sendBusinessStatusUpdateEvent(BusinessStatusUpdateEvent businessStatusUpdateEvent) {
        log.info("Sending business status update message");
        Message<BusinessStatusUpdateEvent> message = MessageBuilder.
                withPayload(businessStatusUpdateEvent)
                .setHeader(KafkaHeaders.TOPIC,"business-status-update-topic")
                .build();
        kafkaTemplateStatusUpdate.send(message);
    }
    public void sendBusinessUpdateEvent(BusinessEvent businessEvent) {
        log.info("Sending business update message");
        Message<BusinessEvent> message = MessageBuilder.
                withPayload(businessEvent)
                .setHeader(KafkaHeaders.TOPIC,"business-update-topic")
                .build();
        kafkaTemplateUpdate.send(message);
    }
}
