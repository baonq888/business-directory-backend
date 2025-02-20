package com.where.location.mapper;

import com.where.location.dto.City;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CityMapper {
    public City mapToCity(Map<String, Object> cityData) {
        City city = new City();
        city.setGeonameId(((Number) cityData.get("geonameId")).longValue());
        city.setName((String) cityData.get("name"));
        city.setCountryCode((String) cityData.get("countryCode"));
        city.setCountryName((String) cityData.get("countryName"));
        city.setAdminName1((String) cityData.get("adminName1")); // State/Province
        city.setLatitude(Double.parseDouble((String) cityData.get("lat")));
        city.setLongitude(Double.parseDouble((String) cityData.get("lng")));
        city.setPopulation(((Number) cityData.get("population")).longValue());

        return city;
    }
}
