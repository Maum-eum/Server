package com.example.springserver.service.score.service;

import com.example.springserver.domain.caregiver.entity.JobCondition;
import com.example.springserver.domain.caregiver.entity.enums.ScheduleAvailability;
import com.example.springserver.domain.caregiver.repository.JobConditionRepository;
import com.example.springserver.domain.center.entity.*;
import com.example.springserver.domain.center.entity.enums.Week;
import com.example.springserver.domain.center.repository.MatchRepository;
import com.example.springserver.domain.center.repository.RecruitCondRepository;
import com.example.springserver.domain.match.entity.Match;
import com.example.springserver.domain.match.entity.enums.MatchStatus;
import com.example.springserver.domain.score.entity.MatchScore;
import com.example.springserver.domain.score.repository.ScoreRepository;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScoreCalculationService {

    private final JobConditionRepository jobConditionRepository;
    private final RecruitCondRepository recruitCondRepository;
    private final MatchRepository matchRepository;
    private final ScoreRepository scoreRepository;

    // 주기적 삭제
    @Transactional
    public void summarize() {
        scoreRepository.deleteAllMarkedAsDeleted();
    }

    // RC 변경시 Update
    @Transactional
    public void recalculateScoresForRecruit(Long recruitConditionId) {
        // 점수 삭제
        scoreRepository.deleteAllByRecruitConditionId(recruitConditionId);

        // RC + RecruitTimes → Map<Week, Long> 구성
        RecruitCondition rc = recruitCondRepository.findById(recruitConditionId)
                .orElseThrow(() -> new GlobalException(ErrorCode.RECRUIT_NOT_FOUND));

        Map<Week, Long> rcDayTimeMap = rc.getRecruitTimes().stream()
                .collect(Collectors.toMap(
                        RecruitTime::getDayOfWeek,
                        rt -> getTimeMask(rt.getStartTime(), rt.getEndTime()),
                        (a, b) -> a | b  // 같은 요일 여러 개면 OR로 합치기
                ));

        int rcDayMask = rcDayTimeMap.keySet().stream()
                .mapToInt(Week::getBitMask)
                .reduce(0, (a, b) -> a | b);

        // JC 목록 조회 (지역 기반 필터링)
        List<JobCondition> candidates = jobConditionRepository.findAllByRecommendedListByElder(rc.getRecruitLocation().getLocationId())
                .orElseThrow(() -> new GlobalException(ErrorCode.RECOMMEND_LIST_NOT_FOUND));

        // Match 조회
        List<Match> matches = matchRepository.findAllByRecruitCondition_RecruitConditionId(rc.getRecruitConditionId());
        Map<Long, Match> matchMap = matches.stream()
                .collect(Collectors.toMap(
                        m -> m.getJobCondition().getId(),
                        m -> m
                ));

        List<MatchScore> results = new ArrayList<>();

        for (JobCondition jc : candidates) {
            if ((jc.getDayOfWeek() & rcDayMask) == 0) continue;

            int conditionScore = calculateConditionScore(jc, rc);
            int timeScore = calculateTimeScore(rcDayTimeMap, jc);
            int finalScore = (conditionScore + timeScore) / 2;

            results.add(MatchScore.builder()
                    .caregiverName(jc.getCaregiver().getName())
                    .caregiverImg(jc.getCaregiver().getImg())
                    .recruitCondition(rc)
                    .jobCondition(jc)
                    .score(finalScore)
                    .status(Optional.ofNullable(matchMap.get(jc.getId()))
                                    .map(Match::getStatus)
                                    .orElse(MatchStatus.NONE))
                    .build());
        }

        scoreRepository.saveAll(results);
    }

    // JC 변경시 Update
    @Transactional
    public void recalculateScoresForJob(Long jobConditionId) {

        // 점수 삭제
        scoreRepository.deleteAllByJobConditionId(jobConditionId);

        // JC 조회
        JobCondition jc = jobConditionRepository.findById(jobConditionId)
                .orElseThrow(() -> new GlobalException(ErrorCode.JOB_CONDITION_NOT_FOUND));

        // JC가 가능한 지역에 있는 RC 후보들 조회
        List<Long> locationIds = jc.getWorkLocations().stream()
                .map(wl -> wl.getLocationId().getLocationId())
                .toList();

        List<RecruitCondition> rcList = recruitCondRepository.findAllByRecruitLocation_LocationIdIn(locationIds);

        // 점수 계산
        List<MatchScore> results = new ArrayList<>();

        for (RecruitCondition rc : rcList) {
            List<RecruitTime> rts = rc.getRecruitTimes();

            // RecruitTime → Map<Week, Long>으로 변환
            Map<Week, Long> rcDayTimeMap = rts.stream()
                    .collect(Collectors.toMap(
                            RecruitTime::getDayOfWeek,
                            rt -> getTimeMask(rt.getStartTime(), rt.getEndTime()),
                            (a, b) -> a | b
                    ));

            int rcDayMask = rcDayTimeMap.keySet().stream()
                    .mapToInt(Week::getBitMask)
                    .reduce(0, (a, b) -> a | b);

            if ((jc.getDayOfWeek() & rcDayMask) == 0) continue;

            int conditionScore = calculateConditionScore(jc, rc);
            int timeScore = calculateTimeScore(rcDayTimeMap, jc);
            int finalScore = (conditionScore + timeScore) / 2;

            MatchStatus match = matchRepository.findByJobCondition_IdAndRecruitCondition_Id(
                    rc.getRecruitConditionId(), jc.getId());

            results.add(MatchScore.builder()
                    .caregiverName(jc.getCaregiver().getName())
                    .caregiverImg(jc.getCaregiver().getImg())
                    .recruitCondition(rc)
                    .score(finalScore)
                    .status(match != null ? match : MatchStatus.NONE)
                    .build());
        }

        scoreRepository.saveAll(results);
    }

    private int calculateConditionScore(JobCondition jc,RecruitCondition rc) {
        int totalScore = 100; // 기본 점수

        // 체크할 필드 목록 (getter 메서드 이름을 기반으로 자동 체크)
        List<String> conditionFields = List.of(
                "SelfFeeding", "MealPreparation", "CookingAssistance", "EnteralNutritionSupport",
                "SelfToileting", "OccasionalToiletingAssist", "DiaperCare", "CatheterOrStomaCare",
                "IndependentMobility", "MobilityAssist", "WheelchairAssist", "Immobile",
                "CleaningLaundryAssist", "BathingAssist", "HospitalAccompaniment",
                "ExerciseSupport", "EmotionalSupport", "CognitiveStimulation"
        );

        try {
            for (String field : conditionFields) {
                Method jcMethod = JobCondition.class.getMethod("get" + field);
                Method rcMethod = RecruitCondition.class.getMethod("is" + field);

                ScheduleAvailability jcValue = (ScheduleAvailability) jcMethod.invoke(jc);
                boolean rcValue = (boolean) rcMethod.invoke(rc);

                if (jcValue == ScheduleAvailability.IMPOSSIBLE && rcValue) {
                    totalScore -= 20;
                } else if (jcValue == ScheduleAvailability.NEGOTIABLE) {
                    totalScore -= 2;
                }
            }
        } catch (Exception e) {
            throw new GlobalException(ErrorCode.ERROR_AT_CALCULATE_LOGIC);
        }
        return Math.max(0, totalScore);
    }

    private int calculateTimeScore(Map<Week, Long> rcDayTimeMap, JobCondition jc) {
        int totalScore = 0;
        int matchedDays = 0;

        long jcTimeMask = getTimeMask(jc.getStartTime(), jc.getEndTime());

        for (Map.Entry<Week, Long> entry : rcDayTimeMap.entrySet()) {
            Week rcDay = entry.getKey();
            long rcTimeMask = entry.getValue();

            // 요일이 겹치는지 확인
            if ((jc.getDayOfWeek() & rcDay.getBitMask()) == 0) continue;

            matchedDays++;

            long overlapped = jcTimeMask & rcTimeMask;

            int overlapCount = Long.bitCount(overlapped);
            int rcCount = Long.bitCount(rcTimeMask);

            int score = (int)((overlapCount / (double) rcCount) * 100);
            totalScore += score;
        }

        return (matchedDays > 0) ? (totalScore / matchedDays) : 0;
    }

    private Long getTimeMask(Long startTime, Long endTime) {
        long mask = 0;
        for (Long i = startTime; i < endTime; i++) {
            mask |= (1L << i);
        }
        return mask;
    }
}
