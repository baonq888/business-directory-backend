package com.where.business.service.geoname;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GeoNameServiceImpl implements GeoNameService {
    @Value("${geonames.username}")
    private String geonamesUsername;
    private final RestTemplate restTemplate = new RestTemplate();
    @Override
    public Optional<Map<String, Object>> getLocationById(Long geoNameId) {
        String url = "http://api.geonames.org/getJSON?geonameId=" + geoNameId + "&username=" + geonamesUsername;
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        return Optional.ofNullable(response);
    }
}
