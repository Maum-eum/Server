package com.example.springserver.global.cache.type;

import java.time.Duration;

public enum RedisCacheType {
    RECRUIT_CACHE("recruitCache", Duration.ofMinutes(2)),
    USER_PROFILE("userProfile", Duration.ofMinutes(5)),
    RECOMMENDATION("recommendation", Duration.ofMinutes(10));

    private final String cacheName;
    private final Duration ttl;

    // 생성자 추가
    RedisCacheType(String cacheName, Duration ttl) {
        this.cacheName = cacheName;
        this.ttl = ttl;
    }

    // getter 추가
    public String getCacheName() {
        return cacheName;
    }

    public Duration getTtl() {
        return ttl;
    }
}
