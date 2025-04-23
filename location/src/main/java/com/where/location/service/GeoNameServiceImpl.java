package com.where.location.service;

import com.where.location.dto.*;
import com.where.location.mapper.CityMapper;
import com.where.location.mapper.CountryMapper;
import com.where.location.mapper.DistrictMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GeoNameServiceImpl implements GeoNameService {

    @Value("${geonames.username}")
    private String geonamesUsername;

    private final CountryMapper countryMapper;
    private final CityMapper cityMapper;
    private final DistrictMapper districtMapper;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<Country> getCountries() {
        String url = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("api.geonames.org")
                .path("/countryInfoJSON")
                .queryParam("username", geonamesUsername)
                .build()
                .toUriString();

        CountryResponse response = restTemplate.getForObject(url, CountryResponse.class);
        return Optional.ofNullable(response)
                .map(CountryResponse::getGeonames)
                .orElse(Collections.emptyList())  // Prevents null issues
                .stream()
                .map(countryMapper::mapToCountry)  // Convert Map<String, Object> to Country entity
                .toList();
    }

    @Override
    public List<City> getCitiesByCountry(String countryCode) {
        String url = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("api.geonames.org")
                .path("/searchJSON")
                .queryParam("country", countryCode)
                .queryParam("featureClass", "P")  // Only cities
                .queryParam("maxRows", "1000")  // Limit results
                .queryParam("username", geonamesUsername)
                .build()
                .toUriString();

        CityResponse response = restTemplate.getForObject(url, CityResponse.class);

        return Optional.ofNullable(response)
                .map(CityResponse::getGeonames)
                .orElse(Collections.emptyList())
                .stream()
                .map(cityMapper::mapToCity)  // Convert JSON map to City object
                .toList();
    }

    @Override
    public List<District> getDistrictsByCity(String countryCode, String cityName) {
        String url = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("api.geonames.org")
                .path("/searchJSON")
                .queryParam("country", countryCode)
                .queryParam("q", cityName)  // Search by city name
                .queryParam("featureCode", "ADM2")  // Districts only
                .queryParam("maxRows", "100")  // Increase limit
                .queryParam("username", geonamesUsername)
                .build()
                .toUriString();

        DistrictResponse response = restTemplate.getForObject(url, DistrictResponse.class);

        return Optional.ofNullable(response)
                .map(DistrictResponse::getGeonames)
                .orElse(Collections.emptyList())
                .stream()
                .map(districtMapper::mapToDistrict)  // Convert JSON map to District object
                .toList();
    }
}
