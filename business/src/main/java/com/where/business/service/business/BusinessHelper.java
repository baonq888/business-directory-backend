package com.where.business.service.business;

import com.where.business.dto.request.BusinessCreateRequest;
import com.where.business.dto.request.BusinessUpdateRequest;
import com.where.business.entity.Business;
import com.where.business.entity.Category;
import com.where.business.entity.ContactInfo;
import com.where.business.enums.BusinessStatus;

public class BusinessHelper {
    public static Business createBusinessFromRequest(BusinessCreateRequest request) {
        Business business = new Business();
        business.setName(request.getName());
        business.setDescription(request.getDescription());
        business.setCategory(new Category(request.getCategoryId()));
        business.setDistrictName(request.getDistrictName());
        business.setCityName(request.getCityName());
        business.setCountryName(request.getCountryName());
        business.setStatus(BusinessStatus.PENDING_APPROVAL);
        return business;
    }


    public static void updateBusinessFromRequest(Business business, BusinessUpdateRequest request) {
        if (request.getName() != null) business.setName(request.getName());
        if (request.getDescription() != null) business.setDescription(request.getDescription());
        if (request.getCategoryId() != null) business.setCategory(new Category(request.getCategoryId()));
        if (request.getDistrictName() != null) business.setDistrictName(request.getDistrictName());
        if (request.getCityName() != null) business.setCityName(request.getCityName());
        if (request.getCountryName() != null) business.setCountryName(request.getCountryName());

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
