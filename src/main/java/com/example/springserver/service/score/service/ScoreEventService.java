package com.example.springserver.service.score.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScoreEventService {

    private final ScoreCalculationService scoreCalculationService;

    @Transactional
    public void summarize() {
        scoreCalculationService.deleteMarkedScores();
    }

    public void recalculateScoresForRecruitWithNewTransaction(Long recruitConditionId) {
        scoreCalculationService.recalculateScoresForRecruit(recruitConditionId);
    }

    public void recalculateScoresForJobWithNewTransaction(Long jobConditionId) {
        scoreCalculationService.recalculateScoresForJob(jobConditionId);
    }
}
