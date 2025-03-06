package com.where.business.service.business;


import com.where.business.dto.request.BusinessCreateRequest;
import com.where.business.entity.Business;

public interface BusinessService {
    Business createBusiness(BusinessCreateRequest request);

}
