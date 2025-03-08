package com.where.search.service;

import com.where.search.document.BusinessDocument;
import com.where.search.dto.request.BusinessSearchRequest;
import org.springframework.data.domain.Page;

public interface SearchService {
    Page<BusinessDocument> searchBusinesses(BusinessSearchRequest request);
    // TODO: Update business Document after Business's status is updated
}
