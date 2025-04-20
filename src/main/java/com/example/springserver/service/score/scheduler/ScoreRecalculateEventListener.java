package com.example.springserver.service.score.scheduler;

import com.example.springserver.domain.caregiver.repository.JobConditionRepository;
import com.example.springserver.service.event.JobConditionChangedEvent;
import com.example.springserver.service.event.RecruitConditionChangedEvent;
import com.example.springserver.service.score.service.ScoreCalculationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScoreRecalculateEventListener {

    private final ApplicationContext applicationContext;
    private final JobConditionRepository jobConditionRepository;

    /**
     * RC 변경된 Event 감지
     * recalculateScoresForRecruitWithNewTransaction 메서드를 통해서
     * 변경에 따른 점수계산으로 들어갑니다.
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleRecruitConditionChanged(RecruitConditionChangedEvent event) {
        log.info("[이벤트 감지] RecruitCondition 변경됨. 점수 업데이트 실행.");

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
        log.info("[이벤트 감지] JobCondition 변경됨. 점수 업데이트 실행.");

        Long jobConditionId = event.getJobConditionId();

        if (!jobConditionRepository.existsById(jobConditionId)) {
            log.warn("[JobCondition Event] 존재하지 않는 jobConditionId({})로 점수 계산을 시도했으나 무시합니다.", jobConditionId);
            return;
        }

        applicationContext.getBean(ScoreCalculationService.class)
                .recalculateScoresForJobWithNewTransaction(event.getJobConditionId());
    }
}
