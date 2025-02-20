package com.where.location.dto;

import lombok.Data;

@Data
public class City {
    private Long geonameId;
    private String name;
    private String countryCode;
    private String countryName;
    private String adminName1; // State/Province
    private Double latitude;
    private Double longitude;
    private Long population;
}
