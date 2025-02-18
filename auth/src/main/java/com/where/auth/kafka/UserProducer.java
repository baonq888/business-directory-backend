package com.where.auth.kafka;

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
    private final KafkaTemplate<String, UserCreateEvent> kafkaTemplate;
    public void sendAppUser(UserCreateEvent userCreateEvent) {
        log.info("Sending user create message");
        Message<UserCreateEvent> message = MessageBuilder
                .withPayload(userCreateEvent)
                .setHeader(KafkaHeaders.TOPIC,"user-create-topic")
                .build();
        kafkaTemplate.send(message);
    }

    public void sendUserRoleUpdate(UserRoleUpdateEvent userRoleUpdateEvent) {
        Message<UserRoleUpdateEvent> message = MessageBuilder
                .withPayload(userRoleUpdateEvent)
                .setHeader(KafkaHeaders.TOPIC,"user-role-update-topic")
                .build();
        kafkaTemplate.send(message);
    }

}
