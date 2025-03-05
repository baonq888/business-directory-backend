package com.where.business.service.business;


import com.where.business.dto.request.BusinessCreateRequest;
import com.where.business.dto.request.BusinessSearchRequest;
import com.where.business.elasticsearch.BusinessDocument;
import com.where.business.entity.Business;
import org.springframework.data.domain.Page;

public interface BusinessService {
    Business createBusiness(BusinessCreateRequest request);
    Page<BusinessDocument> searchBusinesses(BusinessSearchRequest request);
}
