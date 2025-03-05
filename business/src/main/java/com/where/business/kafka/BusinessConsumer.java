package com.where.business.kafka;

import com.where.business.elasticsearch.BusinessDocument;
import com.where.business.repository.BusinessElasticsearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BusinessConsumer {
    private final BusinessElasticsearchRepository businessElasticsearchRepository;

    @KafkaListener(topics = "business-topic", groupId = "business-group")
    public void consumeBusinessCreatedEvent(BusinessEvent event) {
        BusinessDocument businessDocument = BusinessDocument.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .categoryId(event.getCategoryId())
                .districtId(event.getDistrictId())
                .cityId(event.getCityId())
                .countryCode(event.getCountryCode())
                .lat(event.getLat())
                .lon(event.getLon())
                .build();

        businessElasticsearchRepository.save(businessDocument);
    }
}
