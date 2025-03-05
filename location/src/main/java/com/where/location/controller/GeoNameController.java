package com.where.location.controller;

import com.where.location.dto.City;
import com.where.location.dto.Country;
import com.where.location.dto.District;
import com.where.location.service.GeoNameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/location")
@RequiredArgsConstructor
public class GeoNameController {

    private final GeoNameService geoNameService;

    @GetMapping("/countries")
    public ResponseEntity<List<Country>> getCountries() {
        List<Country> countries = geoNameService.getCountries();
        return countries.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(countries);
    }

    @GetMapping("/cities/{countryCode}")
    public ResponseEntity<List<City>> getCitiesByCountry(@PathVariable String countryCode) {
        List<City> cities = geoNameService.getCitiesByCountry(countryCode);
        return cities.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(cities);
    }

    @GetMapping("/districts/{cityName}")
    public ResponseEntity<List<District>> getDistrictsByCity(@PathVariable String cityName) {
        List<District> districts = geoNameService.getDistrictsByCity(cityName);
        return districts.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(districts);
    }
}
