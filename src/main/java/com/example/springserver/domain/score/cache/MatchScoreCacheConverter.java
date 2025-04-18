package com.example.springserver.domain.score.cache;

import com.example.springserver.domain.caregiver.entity.JobCondition;
import com.example.springserver.domain.center.entity.RecruitCondition;
import com.example.springserver.domain.score.entity.MatchScore;
import org.springframework.stereotype.Component;

@Component
public class MatchScoreCacheConverter {

    public MatchScoreCache toCache(MatchScore matchScore) {
        return MatchScoreCache.builder()
                .id(matchScore.getId())
                .recruitConditionId(matchScore.getRecruitCondition().getRecruitConditionId())
                .jobConditionId(matchScore.getJobCondition().getId())
                .score(matchScore.getScore())
                .caregiverName(matchScore.getCaregiverName())
                .caregiverImg(matchScore.getCaregiverImg())
                .status(matchScore.getStatus())
                .build();
    }

    public MatchScore fromCache(MatchScoreCache cache) {
        return MatchScore.builder()
                .id(cache.getId())
                .recruitCondition(RecruitCondition.builder()
                        .recruitConditionId(cache.getRecruitConditionId())
                        .build())
                .jobCondition(JobCondition.builder()
                        .id(cache.getJobConditionId())
                        .build())
                .score(cache.getScore())
                .caregiverName(cache.getCaregiverName())
                .caregiverImg(cache.getCaregiverImg())
                .status(cache.getStatus())
                .build();
    }
}