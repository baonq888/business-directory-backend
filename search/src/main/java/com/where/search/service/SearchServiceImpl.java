package com.where.search.service;

import com.where.search.document.BusinessDocument;
import com.where.search.dto.request.BusinessSearchRequest;
import com.where.search.utils.BusinessSearchQueryBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final ElasticsearchOperations elasticsearchOperations;
    @Override
    public Page<BusinessDocument> searchBusinesses(BusinessSearchRequest request) {
        BoolQuery boolQuery = BusinessSearchQueryBuilder.buildQuery(request);
        Query query = Query.of(q -> q.bool(boolQuery));
        // 🔹 Pagination
        Pageable pageable = PageRequest.of(request.getPage(), request.getLimit());

        // 🔹 Construct Elasticsearch query
        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(query)
                .withPageable(pageable)
                .build();

        // 🔹 Execute search
        SearchHits<BusinessDocument> searchHits = elasticsearchOperations.search(searchQuery, BusinessDocument.class);

        // 🔹 Convert search results
        List<BusinessDocument> results = searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());

        return new PageImpl<>(results, pageable, searchHits.getTotalHits());
    }
}
