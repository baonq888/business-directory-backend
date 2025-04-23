package com.where.location.service;

import com.where.location.dto.City;
import com.where.location.dto.Country;
import com.where.location.dto.District;

import java.util.List;
import java.util.Map;

public interface GeoNameService {
    List<Country> getCountries();
    List<City> getCitiesByCountry(String countryCode);
    List<District> getDistrictsByCity(String countryCode, String cityName);
}
