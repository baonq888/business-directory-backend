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
    private final KafkaTemplate<String, BusinessStatusUpdateEvent> kafkaTemplateStatusUpdate;


    public void sendBusinessStatusUpdateEvent(BusinessStatusUpdateEvent businessStatusUpdateEvent) {
        log.info("Sending business status update message");
        Message<BusinessStatusUpdateEvent> message = MessageBuilder.
                withPayload(businessStatusUpdateEvent)
                .setHeader(KafkaHeaders.TOPIC,"business-status-update-topic")
                .build();
        kafkaTemplateStatusUpdate.send(message);
    }

}
