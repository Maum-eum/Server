package com.example.springserver.service.score.scheduler;

import com.example.springserver.service.score.service.ScoreCalculationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScoreCalculateScheduler {

    private final ScoreCalculationService scoreCalculationService;

    /**
     * 매일 매월 새벽 4시 실행되는 스케줄러 입니다.
     */
    @Scheduled(cron = "0 0 4 * * ?")
    public void calculateDailyScores() {
        log.info("[스케줄러 실행] 매일 새벽 4시, 전체 점수 업데이트 시작...");

        scoreCalculationService.summarize();

        log.info("[스케줄러 실행] 전체 점수 업데이트 완료!");
    }
}
