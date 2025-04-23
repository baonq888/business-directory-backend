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
    private String districtName;
    private String cityName;
    private String countryName;
    private String phone;
    private String email;
    private String website;
    private String status;
}
