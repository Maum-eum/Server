package com.example.springserver;

import com.example.springserver.domain.center.service.RecruitService;
import com.example.springserver.service.score.service.ScoreCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor
public class ScoreDataInitializer implements ApplicationRunner {

    private final ScoreCalculationService scoreCalculationService;
    private final RecruitService recruitService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Long> list = recruitService.findAllRecCond();
        for(Long rcId : list)
            scoreCalculationService.recalculateScoresForRecruit(rcId);
    }
}
