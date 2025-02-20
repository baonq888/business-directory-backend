package com.where.location.mapper;

import com.where.location.dto.Country;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CountryMapper {
    public Country mapToCountry(Map<String, Object> countryData) {
        Country country = new Country();
        country.setCountryCode((String) countryData.get("countryCode"));
        country.setCountryName((String) countryData.get("countryName"));
        country.setCurrencyCode((String) countryData.get("currencyCode"));
        country.setContinentName((String) countryData.get("continentName"));

        return country;
    }
}
