package com.where.location.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DistrictResponse {
    private List<Map<String, Object>> geonames;
}
