package com.where.search.kafka;

import com.where.search.document.BusinessDocument;
import com.where.search.repository.BusinessElasticsearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BusinessConsumer {
    private final BusinessElasticsearchRepository businessElasticsearchRepository;

    @KafkaListener(topics = "business-create-topic", groupId = "business-group")
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
                .status(event.getStatus())
                .build();

        businessElasticsearchRepository.save(businessDocument);
    }

    @KafkaListener(topics = "business-update-topic", groupId = "business-group")
    public void consumeBusinessUpdatedEvent(BusinessEvent event) {
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
                .status(event.getStatus())
                .build();

        businessElasticsearchRepository.save(businessDocument);
    }
}
