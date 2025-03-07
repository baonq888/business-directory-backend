package com.where.auth.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaUserTopicConfig {
    @Bean
    public NewTopic userCreateTopic() {
        return TopicBuilder
                .name("user-create-topic")
                .build();
    }

    @Bean
    public NewTopic userConfirmationTokenTopic() {
        return TopicBuilder
                .name("register-confirmation-token-topic")
                .build();
    }

    @Bean
    public NewTopic userRoleUpdateTopic() {
        return TopicBuilder
                .name("user-role-update-topic")
                .build();
    }

}
