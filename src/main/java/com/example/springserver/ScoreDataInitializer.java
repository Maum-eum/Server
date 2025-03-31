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

    /**
     * MockDataGenerator 쓰면 이게 점수가 안뜨잖아요 Event감지가 안되어서..
     * 그것을 막기위해서 처음에 실행되는 Method예요 존재하는 모든 조합에 대해초반 계산을 시작하게 해요
     * 필요에 의해 주석해제, 주석 할수 있죠
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
//        List<Long> list = recruitService.findAllRecCond();
//        for(Long rcId : list)
//            scoreCalculationService.recalculateScoresForRecruit(rcId);
    }
}
