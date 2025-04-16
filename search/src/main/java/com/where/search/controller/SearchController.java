package com.where.search.controller;

import com.where.search.document.BusinessDocument;
import com.where.search.dto.request.BusinessSearchRequest;
import com.where.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;
    @GetMapping
    public ResponseEntity<Page<BusinessDocument>> searchBusinesses(BusinessSearchRequest request) {
        Page<BusinessDocument> result = searchService.searchBusinesses(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }
}
