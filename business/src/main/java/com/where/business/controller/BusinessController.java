package com.where.business.controller;

import com.where.business.dto.request.BusinessCreateRequest;
import com.where.business.dto.request.BusinessUpdateRequest;
import com.where.business.entity.Business;
import com.where.business.service.business.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("/{id}")
    public ResponseEntity<Business> getBusinessById(@PathVariable Long id) {
        Business business = businessService.getBusinessById(id);
        return ResponseEntity.status(HttpStatus.OK).body(business);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Business> updateBusinessStatus(@PathVariable Long id,
                                                         @RequestParam String status) {
        Business updatedBusiness = businessService.updateBusinessStatus(id, status);
        return ResponseEntity.status(HttpStatus.OK).body(updatedBusiness);
    }

    @GetMapping("/")
    public ResponseEntity<Page<Business>> searchBusinesses(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Business> result = businessService.searchBusinessesByTerm(searchTerm, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Business> updateBusiness(@PathVariable Long id,
                                                   @RequestBody BusinessUpdateRequest request) {
        Business updatedBusiness = businessService.updateBusiness(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(updatedBusiness);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }

}
