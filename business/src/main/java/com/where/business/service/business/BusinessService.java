package com.where.business.service.business;


import com.where.business.dto.request.BusinessCreateRequest;
import com.where.business.dto.request.BusinessUpdateRequest;
import com.where.business.entity.Business;

public interface BusinessService {
    Business createBusiness(BusinessCreateRequest request);
    Business updateBusinessStatus(Long businessId, String newStatus);
    Business getBusinessById(Long businessId);
    Business updateBusiness(Long businessId, BusinessUpdateRequest request);
}
