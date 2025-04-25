package com.where.business.service.business;

import com.where.business.dto.request.BusinessCreateRequest;
import com.where.business.dto.request.BusinessUpdateRequest;
import com.where.business.entity.Business;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;


public interface BusinessService {
    Business createBusiness(BusinessCreateRequest request);
    Business updateBusinessStatus(Long businessId, String newStatus);
    Page<Business> searchBusinessesByTerm(String searchTerm, Pageable pageable);
    Business getBusinessById(Long businessId);
    Business updateBusiness(Long businessId, BusinessUpdateRequest request);
}
