package com.example.springserver.service.score.scheduler;

import com.example.springserver.service.event.JobConditionChangedEvent;
import com.example.springserver.service.event.RecruitConditionChangedEvent;
import com.example.springserver.service.score.service.ScoreCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ScoreRecalculateEventListener {

    private final ApplicationContext applicationContext;

    /**
     * RC 변경된 Event 감지
     * recalculateScoresForRecruitWithNewTransaction 메서드를 통해서
     * 변경에 따른 점수계산으로 들어갑니다.
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleRecruitConditionChanged(RecruitConditionChangedEvent event) {
        System.out.println("[이벤트 감지] RecruitCondition 변경됨. 점수 업데이트 실행.");

        // 추가 및 변경된 RecruitCondition ID를 기준으로 점수 업데이트
        applicationContext.getBean(ScoreCalculationService.class)
                .recalculateScoresForRecruitWithNewTransaction(event.getRecruitConditionId());
    }



    /**
     * JC 변경된 Event 감지
     * recalculateScoresForJobWithNewTransaction 메서드를 통해서
     * 변경에 따른 점수계산으로 들어갑니다.
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleJobConditionChanged(JobConditionChangedEvent event) {
        System.out.println("[이벤트 감지] JobCondition 변경됨. 점수 업데이트 실행.");
        applicationContext.getBean(ScoreCalculationService.class)
                .recalculateScoresForJobWithNewTransaction(event.getJobConditionId());
    }
}
