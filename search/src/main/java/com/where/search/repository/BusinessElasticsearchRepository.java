package com.where.search.repository;

import com.where.search.document.BusinessDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BusinessElasticsearchRepository extends ElasticsearchRepository<BusinessDocument, Long> {

}
