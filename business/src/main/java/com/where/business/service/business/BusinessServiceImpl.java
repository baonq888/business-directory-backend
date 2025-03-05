package com.where.business.service.business;

import com.where.business.elasticsearch.utils.BusinessSearchQueryBuilder;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import com.where.business.dto.request.BusinessCreateRequest;
import com.where.business.dto.request.BusinessSearchRequest;
import com.where.business.elasticsearch.BusinessDocument;
import com.where.business.entity.Business;
import com.where.business.entity.Category;
import com.where.business.kafka.BusinessEvent;
import com.where.business.kafka.BusinessProducer;
import com.where.business.repository.BusinessRepository;
import com.where.business.service.geoname.GeoNameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusinessServiceImpl implements BusinessService{
    private final BusinessRepository businessRepository;
    private final GeoNameService geoNamesService;
    private final BusinessProducer businessProducer;
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    @Transactional
    public Business createBusiness(BusinessCreateRequest request) {
        try {
            Optional<Map<String, Object>> districtData = geoNamesService.getLocationById(request.getDistrictId());
            if (districtData.isEmpty()) {
                throw new IllegalArgumentException("Invalid District ID");
            }

            // Extract city & country from the district
            Long cityId = (Long) districtData.get().get("adminId1"); // City ID from GeoNames
            String countryCode = (String) districtData.get().get("countryCode");

            Business business = new Business();
            business.setName(request.getName());
            business.setDescription(request.getDescription());
            business.setCategory(new Category(request.getCategoryId()));
            business.setDistrictId(request.getDistrictId());
            business.setCityId(cityId);
            business.setCountryCode(countryCode);

            business = businessRepository.save(business);

            BusinessEvent event = BusinessEvent.builder()
                    .id(business.getId())
                    .name(business.getName())
                    .description(business.getDescription())
                    .categoryId(request.getCategoryId())
                    .districtId(request.getDistrictId())
                    .cityId(cityId)
                    .countryCode(countryCode)
                    .lat(request.getLat())
                    .lon(request.getLon())
                    .build();

            businessProducer.sendBusinessCreateEvent(event);

            return business;
        } catch (RuntimeException e) {
            log.error("Error occurred while creating business: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create business", e);
        }

    }

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
