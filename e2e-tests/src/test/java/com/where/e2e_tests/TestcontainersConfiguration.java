package com.where.e2e_tests;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

    @Bean
    public static ApplicationContextInitializer<ConfigurableApplicationContext> initializer() {
        return applicationContext -> TestPropertyValues.of(
                "spring.datasource.url=jdbc:postgresql://localhost:5332/postgres",
                "spring.datasource.username=postgres",
                "spring.datasource.password=password",
                "spring.redis.host=localhost",
                "spring.redis.port=6379",
                "spring.kafka.bootstrap-servers=localhost:9092",
                "spring.elasticsearch.uris=http://localhost:9200",
                "spring.mail.host=localhost",
                "spring.mail.port=1025",
                "spring.zipkin.base-url=http://localhost:9411",
                "spring.sleuth.sampler.probability=1.0"
        ).applyTo(applicationContext.getEnvironment());
    }
}