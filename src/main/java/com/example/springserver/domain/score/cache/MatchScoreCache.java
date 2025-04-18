package com.example.springserver.domain.score.cache;

import com.example.springserver.domain.match.entity.enums.MatchStatus;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash(value = "match_score", timeToLive = 60 * 60) // 1시간 TTL
public class MatchScoreCache implements Serializable {

    @Id
    private Long id;
    private Long recruitConditionId;
    private Long jobConditionId;
    private Integer score;
    private String caregiverName;
    private String caregiverImg;
    private MatchStatus status;
}