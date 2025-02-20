package com.where.location.dto;

import lombok.Data;

@Data
public class District {
    private Long geonameId;
    private String name;
    private String adminCode1;  // State code (e.g., "CA")
    private String adminName1;  // State name (e.g., "California")
    private String countryCode;
    private String countryName;
    private Double latitude;
    private Double longitude;
    private Long population;
}
