package com.example.springserver.domain.location.cache;

import com.example.springserver.global.cache.RedisCacheHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LocationCacheRepository {

    private final RedisCacheHelper cacheHelper;

    private static final String KEY_PREFIX = "location:";
    private static final Duration TTL = Duration.ofDays(30);

    private String getKey(Long id) {
        return KEY_PREFIX + id;
    }

    public void save(LocationCache cache) {
        cacheHelper.saveValue(getKey(cache.getId()), cache, TTL);
    }

    public Optional<LocationCache> findById(Long id) {
        return cacheHelper.get(getKey(id), LocationCache.class);
    }

    public void delete(Long id) {
        cacheHelper.delete(getKey(id));
    }
}
