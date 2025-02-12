package com.example.springserver.service.location;

import com.example.springserver.domain.entity.location.Location;
import com.example.springserver.repository.location.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public List<Location> getLocationList(Long request) {
        return locationRepository.findAllBySigunguId(request);
    }
}
