package com.where.business.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessCreateRequest {
    private String name;
    private String description;
    private Long categoryId;
    private Long districtId;
    private Long cityId;
    private String countryCode;
//    private String lat;
//    private String lon;
    private String phone;
    private String email;
    private String website;
}
