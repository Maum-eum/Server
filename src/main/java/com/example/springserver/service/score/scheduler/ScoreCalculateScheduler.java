package com.example.springserver.service.score.scheduler;

import com.example.springserver.service.score.service.ScoreCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScoreCalculateScheduler {

    private final ScoreCalculationService scoreCalculationService;

    @Scheduled(cron = "0 0 4 * * ?")
    public void calculateDailyScores() {
        System.out.println("[스케줄러 실행] 매일 새벽 4시, 전체 점수 업데이트 시작...");

        scoreCalculationService.summarize();

        System.out.println("[스케줄러 실행] 전체 점수 업데이트 완료!");
    }



}
