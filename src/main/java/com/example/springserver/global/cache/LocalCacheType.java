package com.example.springserver.global.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LocalCacheType { // Caffeine Cache Type
    RECRUIT_CONDITION_CACHE("recruit_condition", 12, 10000),
    JOB_CONDITION_CACHE("job_condition", 12, 10000),
    MATCH_SCORE_CACHE("match_score", 12, 10000);

    private final String cacheName;
    private final int expiredAfterWrite;
    private final int maximumSize;
}