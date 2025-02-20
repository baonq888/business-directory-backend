package com.where.business.service.geoname;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
public class GeoNameServiceImpl implements GeoNameService {
    private final String GEONAMES_USERNAME = "baonq888";
    private final RestTemplate restTemplate = new RestTemplate();
    @Override
    public Optional<Map<String, Object>> getLocationById(Long geoNameId) {
        String url = "http://api.geonames.org/getJSON?geonameId=" + geoNameId + "&username=" + GEONAMES_USERNAME;
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        return Optional.ofNullable(response);
    }
}
