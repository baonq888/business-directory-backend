package com.where.search.kafka;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessEvent {
    private Long id;
    private String name;
    private String description;
    private Long categoryId;
    private Long districtId;
    private Long cityId;
    private String countryCode;
    private String lat;
    private String lon;
    private String status;
}
