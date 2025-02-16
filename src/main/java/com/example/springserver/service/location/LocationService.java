package com.example.springserver.service.location;

import com.example.springserver.domain.location.entity.Location;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.GlobalException;
import com.example.springserver.repository.location.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public List<Location> getLocationList(Long sigunguId) {
        if(sigunguId == 0)
            throw new GlobalException(ErrorCode.BAD_REQUEST);
        return locationRepository.findAllBySigunguId(sigunguId);
    }

    public String getLocation(Long locationId){
        if(locationId == 0)
            throw new GlobalException(ErrorCode.BAD_REQUEST);
        Optional<Location> byId = locationRepository.findById(locationId);
        if(byId.isEmpty())
            throw new GlobalException(ErrorCode.LOCATION_NOT_FOUND);
        return byId.get().getAddress();
    }

    public Location findById(Long locationId) {
        return locationRepository.findById(locationId)
                .orElseThrow(() -> new GlobalException(ErrorCode.LOCATION_NOT_FOUND));
    }
}
