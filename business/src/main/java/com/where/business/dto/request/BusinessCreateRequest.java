package com.where.business.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessCreateRequest {
    private String name;
    private String description;
    private Long categoryId;
    private String districtName;
    private String cityName;
    private String countryName;
    private String phone;
    private String email;
    private String website;
}
