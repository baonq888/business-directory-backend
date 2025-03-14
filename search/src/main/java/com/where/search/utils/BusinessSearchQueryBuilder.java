package com.where.search.utils;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.where.search.dto.request.BusinessSearchRequest;
import lombok.experimental.UtilityClass;
import java.util.Optional;

@UtilityClass
public class BusinessSearchQueryBuilder {

    public static BoolQuery buildQuery(BusinessSearchRequest request) {
        BoolQuery.Builder queryBuilder = QueryBuilders.bool();

        // Full-text search (Name & Description)
        Optional.ofNullable(request.getText()).ifPresent(text ->
                queryBuilder.should(QueryBuilders.match(m -> m.field("name").query(text)))
                        .should(QueryBuilders.match(m -> m.field("description").query(text)))
        );

        // Exact matches (Category, District, City, Country)
        Optional.ofNullable(request.getCategoryId()).ifPresent(categoryId ->
                queryBuilder.must(QueryBuilders.term(t -> t.field("categoryId").value(categoryId)))
        );
        Optional.ofNullable(request.getDistrictId()).ifPresent(districtId ->
                queryBuilder.must(QueryBuilders.term(t -> t.field("districtId").value(districtId)))
        );
        Optional.ofNullable(request.getCityId()).ifPresent(cityId ->
                queryBuilder.must(QueryBuilders.term(t -> t.field("cityId").value(cityId)))
        );
        Optional.ofNullable(request.getCountryCode()).ifPresent(countryCode ->
                queryBuilder.must(QueryBuilders.term(t -> t.field("countryCode").value(countryCode)))
        );

        queryBuilder.must(QueryBuilders.term(t -> t.field("status").value("APPROVED")));


        // Geo-distance filtering
        Double lat = parseDouble(request.getLat());
        Double lon = parseDouble(request.getLon());

        if (lat != null && lon != null) {
            queryBuilder.must(QueryBuilders.geoDistance(g -> g
                    .field("location")
                    .distance("10km") // 🔥 Default 10km radius
                    .location(l -> l.latlon(builder -> builder.lat(lat).lon(lon)))
            ));
        }

        return queryBuilder.build();
    }
    private static Double parseDouble(String value) {
        try {
            return value != null ? Double.parseDouble(value) : null;
        } catch (NumberFormatException e) {
            return null; // Return null if parsing fails
        }
    }
}
