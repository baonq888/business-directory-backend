package com.where.business.service.business;

import com.where.business.dto.request.BusinessUpdateRequest;
import com.where.business.enums.BusinessStatus;
import com.where.business.exception.BusinessException;
import com.where.business.kafka.BusinessStatusUpdateEvent;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import com.where.business.dto.request.BusinessCreateRequest;
import com.where.business.entity.Business;
import com.where.business.kafka.BusinessEvent;
import com.where.business.kafka.BusinessProducer;
import com.where.business.repository.BusinessRepository;
import com.where.business.service.geoname.GeoNameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusinessServiceImpl implements BusinessService{
    private final BusinessRepository businessRepository;
    private final GeoNameService geoNamesService;
    private final BusinessProducer businessProducer;
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    @Transactional
    public Business createBusiness(BusinessCreateRequest request) {
        try {
            Optional<Map<String, Object>> districtData = geoNamesService.getLocationById(request.getDistrictId());
            if (districtData.isEmpty()) {
                throw new IllegalArgumentException("Invalid District ID");
            }

            Long cityId = (Long) districtData.get().get("adminId1"); // City ID from GeoNames
            String countryCode = (String) districtData.get().get("countryCode");

            Business business = BusinessHelper.createBusinessFromRequest(request, cityId, countryCode);

            return businessRepository.save(business);

        } catch (IllegalArgumentException e) {
            log.error("Validation error: {}", e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);

        } catch (Exception e) {
            log.error("Error occurred while creating business: {}", e.getMessage(), e);
            throw new BusinessException("Failed to create business", e);
        }

    }

    @Transactional
    @Override
    public Business updateBusinessStatus(Long businessId, String newStatus) {
        BusinessStatus status = BusinessStatus.valueOf(newStatus.toUpperCase());
        try {
            Business business = businessRepository.findById(businessId)
                    .orElseThrow(() -> new RuntimeException("Business not found with ID: " + businessId));

            business.setStatus(status);
            BusinessStatusUpdateEvent updateEvent = new BusinessStatusUpdateEvent(
                    business.getName(),
                    newStatus
            );
            business = businessRepository.save(business);

            // Send Event to Search Service to persist in Elasticsearch's index
            BusinessEvent businessCreateEvent = BusinessHelper.createBusinessKafkaEvent(business);
            businessProducer.sendBusinessCreateEvent(businessCreateEvent);

            // Send Event to Email Service
            businessProducer.sendBusinessStatusUpdateEvent(updateEvent);
            return business;

        } catch (EntityNotFoundException e) {
            log.error("Business not found: {}", e.getMessage(), e);
            throw e; // Will be handled by GlobalExceptionHandler

        } catch (IllegalArgumentException e) {
            log.error("Invalid status value: {}", newStatus, e);
            throw new BusinessException("Invalid business status: " + newStatus, e);

        } catch (Exception e) {
            log.error("Error occurred while updating business status: {}", e.getMessage(), e);
            throw new BusinessException("Failed to update business status", e);
        }
    }

    @Override
    public Business getBusinessById(Long businessId) {
        try {
            return businessRepository.findById(businessId)
                    .orElseThrow(() -> new EntityNotFoundException("Business not found with ID: " + businessId));
        } catch (EntityNotFoundException e) {
            log.warn("Business not found with ID: {}", businessId);
            throw new BusinessException("Business not found with ID: " + businessId, e);
        } catch (Exception e) {
            log.error("Error while fetching business {}: {}", businessId, e.getMessage(), e);
            throw new BusinessException("Failed to retrieve business", e);
        }
    }

    @Override
    public Business updateBusiness(Long businessId, BusinessUpdateRequest request) {
        try {
            Business business = businessRepository.findById(businessId)
                    .orElseThrow(() -> new EntityNotFoundException("Business not found"));

            BusinessHelper.updateBusinessFromRequest(business, request);

            // Send to search-service to update Business document from elasticsearch
            BusinessEvent businessUpdateEvent = BusinessHelper.createBusinessKafkaEvent(business);

            businessProducer.sendBusinessUpdateEvent(businessUpdateEvent);
            return businessRepository.save(business);

        } catch (EntityNotFoundException e) {
            log.warn("Business not found: {}", businessId);
            throw new BusinessException("Business not found with ID: " + businessId, e);
        } catch (IllegalArgumentException e) {
            log.error("Invalid status value for business {}: {}", businessId, request.getStatus(), e);
            throw new BusinessException("Invalid status value: " + request.getStatus(), e);
        } catch (Exception e) {
            log.error("Unexpected error while updating business: {}", e.getMessage(), e);
            throw new BusinessException("Failed to update business: " + e.getMessage(), e);
        }
    }


}
