package com.where.business.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateRequest {
    private Long businessId;
    private int rating;
    private String comment;
    private Long reviewerId;
    private String name;
    private String email;
}