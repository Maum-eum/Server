package com.example.springserver.domain.location.cache;

import com.example.springserver.domain.location.entity.Location;

public class LocationCacheConverter {

    public static LocationCache toCache(Location location) {
        return LocationCache.builder()
                .id(location.getLocationId())
                .address(location.getAddress())
                .build();
    }
}