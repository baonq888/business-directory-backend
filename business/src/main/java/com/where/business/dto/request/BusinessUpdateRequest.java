package com.where.business.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BusinessUpdateRequest {
    private String name;
    private String description;
    private Long categoryId;
    private Long districtId;
    private Long cityId;
    private String countryCode;
    private String lat;
    private String lon;
    private String phone;
    private String email;
    private String website;
    private String status;
}
