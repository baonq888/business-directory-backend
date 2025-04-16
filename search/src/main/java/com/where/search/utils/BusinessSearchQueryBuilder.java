package com.where.search.utils;

import co.elastic.clients.elasticsearch._types.query_dsl.*;
import com.where.search.dto.request.BusinessSearchRequest;

import java.util.Optional;

public class BusinessSearchQueryBuilder {

    public static Query buildQuery(BusinessSearchRequest request) {
        return Query.of(q -> q.bool(b -> {
            // Start building the BoolQuery
            BoolQuery.Builder builder = new BoolQuery.Builder();

            // Full-text search (Name & Description)
            Optional.ofNullable(request.getText()).ifPresent(text -> {
                builder.should(s -> s.match(m -> m.field("name").query(text)));
                builder.should(s -> s.match(m -> m.field("description").query(text)));
            });

            // Exact matches (Category, District, City, Country)
            Optional.ofNullable(request.getCategoryId()).ifPresent(categoryId ->
                    builder.must(m -> m.term(t -> t.field("categoryId").value(categoryId)))
            );
            Optional.ofNullable(request.getDistrictId()).ifPresent(districtId ->
                    builder.must(m -> m.term(t -> t.field("districtId").value(districtId)))
            );
            Optional.ofNullable(request.getCityId()).ifPresent(cityId ->
                    builder.must(m -> m.term(t -> t.field("cityId").value(cityId)))
            );
            Optional.ofNullable(request.getCountryCode()).ifPresent(countryCode ->
                    builder.must(m -> m.term(t -> t.field("countryCode").value(countryCode)))
            );

            // Status filter
            builder.must(m -> m.term(t -> t.field("status").value("APPROVED")));

            // Geo-distance filtering
            Double lat = parseDouble(request.getLat());
            Double lon = parseDouble(request.getLon());

            if (lat != null && lon != null) {
                builder.must(m -> m.geoDistance(g -> g
                        .field("location")
                        .distance("10km") // 🔥 Default 10km radius
                        .location(l -> l.latlon(geo -> geo.lat(lat).lon(lon)))
                ));
            }

            return b;
        }));
    }

    private static Double parseDouble(String value) {
        try {
            return value != null ? Double.parseDouble(value) : null;
        } catch (NumberFormatException e) {
            return null; // Return null if parsing fails
        }
    }
}
