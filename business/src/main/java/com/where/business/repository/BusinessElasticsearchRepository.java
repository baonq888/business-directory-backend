package com.where.business.repository;

import com.where.business.elasticsearch.BusinessDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BusinessElasticsearchRepository extends ElasticsearchRepository<BusinessDocument, Long> {
    SearchHits<BusinessDocument> findByCustomQuery(String text, String name, String description, Long categoryId,
                                                   Long districtId, Long cityId, String countryCode,
                                                   Double lat, Double lon, Pageable pageable);
}
