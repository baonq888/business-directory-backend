package com.where.business.service.business;

import com.where.business.dto.request.BusinessCreateRequest;
import com.where.business.dto.request.BusinessUpdateRequest;
import com.where.business.entity.Business;
import com.where.business.entity.Category;
import com.where.business.entity.ContactInfo;
import com.where.business.enums.BusinessStatus;
import com.where.business.kafka.BusinessEvent;

public class BusinessHelper {
    public static Business createBusinessFromRequest(BusinessCreateRequest request, Long cityId, String countryCode) {
        Business business = new Business();
        business.setName(request.getName());
        business.setDescription(request.getDescription());
        business.setCategory(new Category(request.getCategoryId()));
        business.setDistrictId(request.getDistrictId());
        business.setCityId(cityId);
//        business.setLat(request.getLat());
//        business.setLon(request.getLon());
        business.setCountryCode(countryCode);
        business.setStatus(BusinessStatus.PENDING_APPROVAL);
        return business;
    }

    public static BusinessEvent createBusinessKafkaEvent(Business business) {
        return BusinessEvent.builder()
                .id(business.getId())
                .name(business.getName())
                .description(business.getDescription())
                .categoryId(business.getCategory().getId())
                .districtId(business.getDistrictId())
                .cityId(business.getCityId())
                .countryCode(business.getCountryCode())
                .lat(business.getLat())
                .lon(business.getLon())
                .status(BusinessStatus.PENDING_APPROVAL.toString())
                .build();
    }

    public static void updateBusinessFromRequest(Business business, BusinessUpdateRequest request) {
        if (request.getName() != null) business.setName(request.getName());
        if (request.getDescription() != null) business.setDescription(request.getDescription());
        if (request.getCategoryId() != null) business.setCategory(new Category(request.getCategoryId()));
        if (request.getDistrictId() != null) business.setDistrictId(request.getDistrictId());
        if (request.getCityId() != null) business.setCityId(request.getCityId());
        if (request.getCountryCode() != null) business.setCountryCode(request.getCountryCode());
        if (request.getLat() != null) business.setLat(request.getLat());
        if (request.getLon() != null) business.setLon(request.getLon());

        updateContactInfo(business, request);

        if (request.getStatus() != null) {
            try {
                business.setStatus(BusinessStatus.valueOf(request.getStatus()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status value: " + request.getStatus(), e);
            }
        }
    }

    private static void updateContactInfo(Business business, BusinessUpdateRequest request) {
        ContactInfo contactInfo = business.getContactInfo();
        if (contactInfo == null) {
            contactInfo = new ContactInfo();
        }
        if (request.getPhone() != null) contactInfo.setPhone(request.getPhone());
        if (request.getEmail() != null) contactInfo.setEmail(request.getEmail());
        if (request.getWebsite() != null) contactInfo.setWebsite(request.getWebsite());
        business.setContactInfo(contactInfo);
    }
}
