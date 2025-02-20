package com.where.business.service.geoname;

import java.util.Map;
import java.util.Optional;

public interface GeoNameService {
    Optional<Map<String, Object>> getLocationById(Long geoNameId);
}
