package com.example.springserver.application;

import com.example.springserver.MockDataGenerator;
import com.example.springserver.domain.caregiver.repository.JobConditionRepository;
import com.example.springserver.domain.center.entity.RecruitCondition;
import com.example.springserver.domain.center.repository.RecruitConditionRepository;
import com.example.springserver.domain.score.entity.MatchScore;
import com.example.springserver.domain.score.repository.ScoreRepository;
import com.example.springserver.service.score.service.ScoreCalculationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@TestPropertySource("classpath:.env.test.properties")
@SpringBootTest
class ScoreCalculationServiceTest {

    @Autowired
    private MockDataGenerator mockDataGenerator;

    @Autowired
    private JobConditionRepository jobConditionRepository;

    @Autowired
    private RecruitConditionRepository recruitConditionRepository;

    @Autowired
    private ScoreCalculationService scoreCalculationService;

    @Autowired
    private ScoreRepository scoreRepository;

    @BeforeEach
    void setUp() {
        mockDataGenerator.createMockData(); // 목 데이터 생성
    }

    @Test
    @Transactional
    void testRecalculateScoresForRecruit() {
        // given
        RecruitCondition recruitCondition = recruitConditionRepository.findAll().get(0);
        Long recruitConditionId = recruitCondition.getRecruitConditionId();

        // when
        scoreCalculationService.recalculateScoresForRecruit(recruitConditionId);

        // then
        List<MatchScore> scores = scoreRepository.findAllByRecruitConditionIncludingDeleted(recruitConditionId);

        assertTrue(!scores.isEmpty(), "RecruitCondition에 대한 MatchScore 생성 성공");

        for (MatchScore score : scores) {
            assertEquals(recruitConditionId, score.getRecruitCondition().getRecruitConditionId());
            assertNotNull(score.getScore());
            assertNotNull(score.getJobCondition());
        }
    }

//    @Test
//    void testRecalculateScoresForJob() {
//        // given
//        JobCondition job = jobConditionRepository.findAll().get(0);
//        Long jobConditionId = job.getId();
//
//        // when
//        scoreCalculationService.recalculateScoresForJob(jobConditionId);
//
//        // then
//        List<MatchScore> scores = scoreRepository.findAllByJobConditionIncludingDeleted(jobConditionId);
//
//        assertTrue(!scores.isEmpty(), "JobCondition에 대한 MatchScore 생성 성공");
//        for (MatchScore score : scores) {
//            assertEquals(jobConditionId, score.getJobCondition().getId());
//            assertNotNull(score.getScore());
//            assertNotNull(score.getRecruitCondition());
//        }
//    }
}
