package com.where.location.mapper;

import com.where.location.dto.District;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DistrictMapper {
    public District mapToDistrict(Map<String, Object> districtData) {
        District district = new District();
        district.setGeonameId(((Number) districtData.get("geonameId")).longValue());
        district.setName((String) districtData.get("name"));
        district.setAdminCode1((String) districtData.get("adminCode1")); // State code (e.g., CA)
        district.setAdminName1((String) districtData.get("adminName1")); // State name (e.g., California)
        district.setCountryCode((String) districtData.get("countryCode"));
        district.setCountryName((String) districtData.get("countryName"));
        district.setLatitude(Double.parseDouble((String) districtData.get("lat")));
        district.setLongitude(Double.parseDouble((String) districtData.get("lng")));
        district.setPopulation(((Number) districtData.get("population")).longValue());

        return district;
    }
}
