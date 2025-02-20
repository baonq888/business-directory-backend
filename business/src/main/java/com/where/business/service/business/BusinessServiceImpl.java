package com.where.business.service.business;

import com.where.business.dto.request.BusinessCreateRequest;
import com.where.business.entity.Business;
import com.where.business.entity.Category;
import com.where.business.repository.BusinessRepository;
import com.where.business.service.geoname.GeoNameService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService{
    private final BusinessRepository businessRepository;
    private final GeoNameService geoNamesService;

    @Override
    @Transactional
    public Business createBusiness(BusinessCreateRequest request) {

        Optional<Map<String, Object>> districtData = geoNamesService.getLocationById(request.getDistrictId());
        if (districtData.isEmpty()) {
            throw new IllegalArgumentException("Invalid District ID");
        }

        // Extract city & country from the district
        Long cityId = (Long) districtData.get().get("adminId1"); // City ID from GeoNames
        String countryCode = (String) districtData.get().get("countryCode");

        Business business = new Business();
        business.setName(request.getName());
        business.setDescription(request.getDescription());
        business.setCategory(new Category(request.getCategoryId()));
        business.setDistrictId(request.getDistrictId());
        business.setCityId(cityId);
        business.setCountryCode(countryCode);

        return businessRepository.save(business);
    }
}
