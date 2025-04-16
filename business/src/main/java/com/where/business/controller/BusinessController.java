package com.where.business.controller;

import com.where.business.dto.request.BusinessCreateRequest;
import com.where.business.entity.Business;
import com.where.business.service.business.BusinessService;
import lombok.RequiredArgsConstructor;
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

    @PatchMapping("/{id}/status")
    public ResponseEntity<Business> updateBusinessStatus(@PathVariable Long id,
                                                         @RequestParam String status) {
        Business updatedBusiness = businessService.updateBusinessStatus(id, status);
        return ResponseEntity.ok(updatedBusiness);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }

}
