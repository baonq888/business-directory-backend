package com.where.business.controller;

import com.where.business.dto.request.BusinessCreateRequest;
import com.where.business.dto.request.BusinessSearchRequest;
import com.where.business.elasticsearch.BusinessDocument;
import com.where.business.entity.Business;
import com.where.business.service.business.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/business")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;

    @PostMapping
    public ResponseEntity<Business> createBusiness(@RequestBody BusinessCreateRequest request) {
        Business business = businessService.createBusiness(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(business);
    }

    @GetMapping
    public ResponseEntity<Page<BusinessDocument>> searchBusinesses(BusinessSearchRequest request) {
        Page<BusinessDocument> result = businessService.searchBusinesses(request);
        return ResponseEntity.ok(result);
    }
}
