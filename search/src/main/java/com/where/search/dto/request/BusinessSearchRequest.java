package com.where.search.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessSearchRequest {
    private String text;
    private String name;
    private String description;
    private Long categoryId;
    private Long districtId;
    private Long cityId;
    private String countryCode;
    private String lat;
    private String lon;
    private Integer page = 0;
    private Integer limit = 10;
    private String sortBy = "name";
}
