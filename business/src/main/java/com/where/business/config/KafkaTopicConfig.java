package com.where.business.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic businessCreateTopic() {
        return TopicBuilder
                .name("business-create-topic")
                .build();
    }

    @Bean
    public NewTopic businessStatusUpdateTopic() {
        return TopicBuilder
                .name("business-status-update-topic")
                .build();
    }

    @Bean
    public NewTopic businessUpdateTopic() {
        return TopicBuilder
                .name("business-update-topic")
                .build();
    }
}
