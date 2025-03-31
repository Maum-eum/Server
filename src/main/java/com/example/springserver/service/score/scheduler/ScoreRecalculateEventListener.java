package com.example.springserver.service.score.scheduler;

import com.example.springserver.service.event.JobConditionChangedEvent;
import com.example.springserver.service.event.RecruitConditionChangedEvent;
import com.example.springserver.service.score.service.ScoreCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScoreRecalculateEventListener {

    private final ScoreCalculationService scoreCalculationService;

    @Async
    @EventListener
    public void handleRecruitConditionChanged(RecruitConditionChangedEvent event) {
        System.out.println("[이벤트 감지] RecruitCondition 변경됨. 점수 업데이트 실행.");

        // 변경된 RecruitCondition ID를 기준으로 점수 업데이트
        scoreCalculationService.recalculateScoresForRecruit(event.getRecruitConditionId());
    }

    @Async
    @EventListener
    public void handleJobConditionChanged(JobConditionChangedEvent event) {
        System.out.println("[이벤트 감지] JobCondition 변경됨. 점수 업데이트 실행.");

        // 변경된 RecruitCondition ID를 기준으로 점수 업데이트
        scoreCalculationService.recalculateScoresForJob(event.getJobConditionId());
    }
}
