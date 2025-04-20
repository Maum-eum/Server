package com.example.springserver.service.score.service;

import com.example.springserver.domain.caregiver.entity.JobCondition;
import com.example.springserver.domain.caregiver.entity.enums.ScheduleAvailability;
import com.example.springserver.domain.caregiver.repository.JobConditionRepository;
import com.example.springserver.domain.center.entity.RecruitCondition;
import com.example.springserver.domain.center.entity.RecruitTime;
import com.example.springserver.domain.center.entity.enums.Week;
import com.example.springserver.domain.center.repository.MatchRepository;
import com.example.springserver.domain.center.repository.RecruitConditionRepository;
import com.example.springserver.domain.match.entity.Match;
import com.example.springserver.domain.match.entity.enums.MatchStatus;
import com.example.springserver.domain.score.entity.MatchScore;
import com.example.springserver.domain.score.repository.ScoreRepository;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScoreCalculationService {

    private final JobConditionRepository jobConditionRepository;
    private final RecruitConditionRepository recruitConditionRepository;
    private final MatchRepository matchRepository;
    private final ScoreRepository scoreRepository;

    // 주기적 삭제
    /**
     * 주기적으로 스케줄러에서 호출되는 메서드입니다.
     * deledted_at 이 NULL 이 아니라면 삭제하는 메서드입니다.
     */
    @Transactional
    public void summarize() {
        try {
            scoreRepository.deleteAllMarkedAsDeleted();
        } catch (Exception e) {
            log.error("scoreRepository.deleteAllMarkedAsDeleted 실패", e);
            throw new GlobalException(ErrorCode.INTERNAL_SERVER_ERROR_DELETE_MATCH_SCORE);
        }
    }

    /**
     *  RC 변경시 점수 재계산 때립니다.
     *  기존 MatchScore가 존재하면 복구 및 업데이트,
     *  존재하지 않으면 새로 생성합니다.
     */
    //rc 변경시 update
    public void recalculateScoresForRecruit(Long recruitConditionId) {
        RecruitCondition rc = fetchRecruitCondition(recruitConditionId);
        Map<Week, Long> rcDayTimeMap = buildRcDayTimeMap(rc);
        int rcDayMask = calculateRcDayMask(rcDayTimeMap);

        List<JobCondition> candidates = fetchJobConditionCandidatesByLocation(rc);
        Map<String, MatchScore> existingScoreMap = fetchExistingMatchScoresMapForRecruit(rc.getRecruitConditionId());
        Map<Long, Match> matchMap = fetchMatchStatusMapForRecruit(rc.getRecruitConditionId());

        List<MatchScore> results = calculateMatchScoresForRecruit(candidates, rc, rcDayTimeMap, rcDayMask, existingScoreMap, matchMap);
        List<MatchScore> toSoftDelete = filterSoftDeleteTargetsForRecruit(existingScoreMap, results);

        results.addAll(toSoftDelete);
        scoreRepository.saveAll(results);
    }

    // JC 변경시 Update
    /**
     * JC 변경 시 점수 재계산을 수행합니다.
     * 존재하는 MatchScore는 수정/복구하고, 존재하지 않으면 새로 생성하여 저장합니다.
     */
    public void recalculateScoresForJob(Long jobConditionId) {
        try {
            JobCondition jc = fetchJobCondition(jobConditionId);
            List<RecruitCondition> rcList = fetchRelatedRecruitConditions(jc);
            Map<String, MatchScore> existingScoreMap = fetchExistingMatchScoreMap(jobConditionId);

            List<MatchScore> results = new ArrayList<>();

            for (RecruitCondition rc : rcList) {
                Map<Week, Long> rcDayTimeMap = buildRcDayTimeMap(rc.getRecruitTimes());

                if (!isAvailableOnSameDay(jc, rcDayTimeMap)) continue;

                int conditionScore = calculateConditionScore(jc, rc);
                int timeScore = calculateTimeScore(rcDayTimeMap, jc);
                int finalScore = (conditionScore + timeScore) / 2;

                MatchStatus match = matchRepository.findByJobCondition_IdAndRecruitCondition_Id(
                        rc.getRecruitConditionId(), jc.getId());

                String key = generateKey(jc.getId(), rc.getRecruitConditionId());

                if (existingScoreMap.containsKey(key)) {
                    MatchScore updated = updateExistingScore(existingScoreMap.get(key), match, finalScore);
                    results.add(updated);
                } else {
                    MatchScore created = createNewScore(jc, rc, finalScore, match);
                    results.add(created);
                }
            }

            Set<String> processedKeys = results.stream()
                    .map(ms -> generateKey(ms.getJobCondition().getId(), ms.getRecruitCondition().getRecruitConditionId()))
                    .collect(Collectors.toSet());

            List<MatchScore> toSoftDelete = existingScoreMap.entrySet().stream()
                    .filter(entry -> !processedKeys.contains(entry.getKey()))
                    .map(Map.Entry::getValue)
                    .filter(ms -> ms.getDeletedAt() == null)
                    .map(ms -> {
                        ms.setDeletedAt(LocalDateTime.now());
                        return ms;
                    })
                    .toList();

            results.addAll(toSoftDelete);
            scoreRepository.saveAll(results);
        } catch (Exception e) {
            log.error("recalculateScoresForJob 실패: jobConditionId = {}", jobConditionId, e);
            throw new GlobalException(ErrorCode.JCSCORE_RECALCULATING_FAIL);
        }
    }

    /**
     * 주어진 ID로 RecruitCondition을 조회합니다.
     * 존재하지 않으면 예외를 발생시킵니다.
     */
    private RecruitCondition fetchRecruitCondition(Long recruitConditionId) {
        return recruitConditionRepository.findById(recruitConditionId)
                .orElseThrow(() -> new GlobalException(ErrorCode.RECRUIT_NOT_FOUND));
    }

    /**
     * RecruitCondition에 등록된 RecruitTime들을 기반으로
     * 요일별 시간 마스크(Map<Week, Long>)를 생성합니다.
     */
    private Map<Week, Long> buildRcDayTimeMap(RecruitCondition rc) {
        return rc.getRecruitTimes().stream()
                .collect(Collectors.toMap(
                        RecruitTime::getDayOfWeek,
                        rt -> getTimeMask(rt.getStartTime(), rt.getEndTime()),
                        (a, b) -> a | b
                ));
    }

    /**
     * RC가 가능한 요일 목록을 비트 OR 연산을 통해 하나의 int 마스크로 반환합니다.
     * (ex: 월/수/금 → 0b0010101)
     */
    private int calculateRcDayMask(Map<Week, Long> rcDayTimeMap) {
        return rcDayTimeMap.keySet().stream()
                .mapToInt(Week::getBitMask)
                .reduce(0, (a, b) -> a | b);
    }

    /**
     * RecruitCondition의 근무 지역을 기준으로 추천 가능한 JobCondition 리스트를 조회합니다.
     * 존재하지 않으면 예외를 발생시킵니다.
     */
    private List<JobCondition> fetchJobConditionCandidatesByLocation(RecruitCondition rc) {
        return jobConditionRepository.findAllByRecommendedListByElder(
                rc.getRecruitLocation().getLocationId()
        ).orElseThrow(() -> new GlobalException(ErrorCode.RECOMMEND_LIST_NOT_FOUND));
    }

    /**
     * RecruitCondition 기준으로 기존 MatchScore 리스트를 조회합니다.
     * soft-delete 포함하며, (jobId_rcId) 형태의 Key로 Map을 구성해 반환합니다.
     */
    private Map<String, MatchScore> fetchExistingMatchScoresMapForRecruit(Long recruitConditionId) {
        List<MatchScore> existing = scoreRepository.findAllByRecruitConditionIncludingDeleted(recruitConditionId);
        return existing.stream().collect(Collectors.toMap(
                ms -> generateKey(ms.getJobCondition().getId(), ms.getRecruitCondition().getRecruitConditionId()),
                ms -> ms
        ));
    }

    /**
     * RecruitCondition 기준으로 기존 Match 리스트를 조회하고
     * JobCondition ID를 키로 하는 Map 형태로 반환합니다.
     */
    private Map<Long, Match> fetchMatchStatusMapForRecruit(Long recruitConditionId) {
        List<Match> matches = matchRepository.findAllByRecruitCondition_RecruitConditionId(recruitConditionId);
        return matches.stream().collect(Collectors.toMap(
                m -> m.getJobCondition().getId(),
                m -> m
        ));
    }

    /**
     * 주어진 JobCondition 후보 리스트와 RecruitCondition에 대해
     * 유효한 점수를 계산하여 MatchScore 리스트로 반환합니다.
     * 이미 존재하는 MatchScore는 복구/갱신, 없으면 새로 생성합니다.
     */
    private List<MatchScore> calculateMatchScoresForRecruit(
            List<JobCondition> candidates,
            RecruitCondition rc,
            Map<Week, Long> rcDayTimeMap,
            int rcDayMask,
            Map<String, MatchScore> existingScoreMap,
            Map<Long, Match> matchMap
    ) {
        List<MatchScore> results = new ArrayList<>();

        for (JobCondition jc : candidates) {
            if ((jc.getDayOfWeek() & rcDayMask) == 0) continue;

            int conditionScore = calculateConditionScore(jc, rc);
            int timeScore = calculateTimeScore(rcDayTimeMap, jc);
            int finalScore = (conditionScore + timeScore) / 2;

            String key = jc.getId() + "_" + rc.getRecruitConditionId();

            if (existingScoreMap.containsKey(key)) {
                MatchScore existing = existingScoreMap.get(key);
                existing.setScore(finalScore);
                existing.setStatus(Optional.ofNullable(matchMap.get(jc.getId()))
                        .map(Match::getStatus)
                        .orElse(MatchStatus.NONE));
                existing.setDeletedAt(null);
                results.add(existing);
            } else {
                MatchScore newScore = MatchScore.builder()
                        .caregiverName(jc.getCaregiver().getName())
                        .caregiverImg(jc.getCaregiver().getImg())
                        .recruitCondition(rc)
                        .jobCondition(jc)
                        .score(finalScore)
                        .status(Optional.ofNullable(matchMap.get(jc.getId()))
                                .map(Match::getStatus)
                                .orElse(MatchStatus.NONE))
                        .build();
                results.add(newScore);
            }
        }
        return results;
    }

    /**
     * 기존에 존재하던 MatchScore 중 이번 점수 재계산 결과에 포함되지 않은 항목을 필터링합니다.
     * 해당 항목들은 soft-delete 처리를 위해 deletedAt을 설정하여 반환합니다.
     */
    private List<MatchScore> filterSoftDeleteTargetsForRecruit(
            Map<String, MatchScore> existingScoreMap,
            List<MatchScore> newScores
    ) {
        Set<String> processedKeys = newScores.stream()
                .map(ms -> generateKey(ms.getJobCondition().getId(), ms.getRecruitCondition().getRecruitConditionId()))
                .collect(Collectors.toSet());

        return existingScoreMap.entrySet().stream()
                .filter(entry -> !processedKeys.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .filter(ms -> ms.getDeletedAt() == null)
                .map(ms -> {
                    ms.setDeletedAt(LocalDateTime.now());
                    return ms;
                })
                .toList();
    }

    /**
     * JC ID로 JobCondition을 조회합니다.
     * 존재하지 않으면 예외를 발생시킵니다.
     */
    private JobCondition fetchJobCondition(Long jobConditionId) {
        log.info("jobConditionId = {}", jobConditionId);
        return jobConditionRepository.findById(jobConditionId)
                .orElseThrow(() -> new GlobalException(ErrorCode.JOB_CONDITION_NOT_FOUND));
    }

    /**
     * JC가 가능한 지역 기반으로 관련 RecruitCondition을 조회합니다.
     */
    private List<RecruitCondition> fetchRelatedRecruitConditions(JobCondition jc) {
        List<Long> locationIds = jc.getWorkLocations().stream()
                .map(wl -> wl.getLocation().getLocationId())
                .toList();
        return recruitConditionRepository.findAllByRecruitLocation_LocationIdIn(locationIds);
    }

    /**
     * soft-delete 포함하여, 해당 JobCondition과 관련된 모든 MatchScore를 Map 형태로 조회합니다.
     */
    private Map<String, MatchScore> fetchExistingMatchScoreMap(Long jobConditionId) {
        List<MatchScore> scores = scoreRepository.findAllByJobConditionIncludingDeleted(jobConditionId);
        return scores.stream().collect(Collectors.toMap(
                ms -> generateKey(ms.getJobCondition().getId(), ms.getRecruitCondition().getRecruitConditionId()),
                ms -> ms
        ));
    }

    /**
     * RecruitTime 리스트로부터 요일-비트마스크 맵을 생성합니다.
     */
    private Map<Week, Long> buildRcDayTimeMap(List<RecruitTime> recruitTimes) {
        return recruitTimes.stream()
                .collect(Collectors.toMap(
                        RecruitTime::getDayOfWeek,
                        rt -> getTimeMask(rt.getStartTime(), rt.getEndTime()),
                        (a, b) -> a | b
                ));
    }

    /**
     * JC와 RC가 가능한 요일이 하나라도 겹치는지 확인합니다.
     */
    private boolean isAvailableOnSameDay(JobCondition jc, Map<Week, Long> rcDayTimeMap) {
        int rcDayMask = rcDayTimeMap.keySet().stream()
                .mapToInt(Week::getBitMask)
                .reduce(0, (a, b) -> a | b);
        return (jc.getDayOfWeek() & rcDayMask) != 0;
    }

    /**
     * MatchScore를 새로 생성합니다.
     */
    private MatchScore createNewScore(JobCondition jc, RecruitCondition rc, int score, MatchStatus match) {
        return MatchScore.builder()
                .caregiverName(jc.getCaregiver().getName())
                .caregiverImg(jc.getCaregiver().getImg())
                .recruitCondition(rc)
                .jobCondition(jc)
                .score(score)
                .status(match != null ? match : MatchStatus.NONE)
                .build();
    }

    /**
     * 기존 MatchScore를 업데이트 및 복원(soft-delete 해제)합니다.
     */
    private MatchScore updateExistingScore(MatchScore existing, MatchStatus match, int newScore) {
        existing.setScore(newScore);
        existing.setStatus(match != null ? match : MatchStatus.NONE);
        existing.setDeletedAt(null); // 복원
        return existing;
    }

    /**
     * jobConditionId와 recruitConditionId로 고유 key를 생성합니다.
     */
    private String generateKey(Long jobConditionId, Long recruitConditionId) {
        return jobConditionId + "_" + recruitConditionId;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recalculateScoresForRecruitWithNewTransaction(Long recruitConditionId) {
        try {
            recalculateScoresForRecruit(recruitConditionId);
        } catch (Exception e) {
            throw new GlobalException(ErrorCode.RCSCORE_RECALCULATING_FAIL);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recalculateScoresForJobWithNewTransaction(Long jobConditionId) {
        try {
            recalculateScoresForJob(jobConditionId);
        } catch (Exception e) {
            throw new GlobalException(ErrorCode.JCSCORE_RECALCULATING_FAIL);
        }
    }

    private int calculateConditionScore(JobCondition jc, RecruitCondition rc) {
        int totalScore = 100; // 기본 점수

        // 체크할 필드 목록
        List<String> conditionFields = List.of(
                "SelfFeeding", "MealPreparation", "CookingAssistance", "EnteralNutritionSupport",
                "SelfToileting", "OccasionalToiletingAssist", "DiaperCare", "CatheterOrStomaCare",
                "IndependentMobility", "MobilityAssist", "WheelchairAssist", "Immobile",
                "CleaningLaundryAssist", "BathingAssist", "HospitalAccompaniment",
                "ExerciseSupport", "EmotionalSupport", "CognitiveStimulation"
        );

        for (String field : conditionFields) {
            totalScore = updateScoreBasedOnCondition(jc, rc, field, totalScore);
        }

        return Math.max(0, totalScore);
    }

    /**
     * 필드에 따른 점수를 업데이트합니다.
     */
    private int updateScoreBasedOnCondition(JobCondition jc, RecruitCondition rc, String field, int totalScore) {
        try {
            // 필드별 getter 메서드 호출
            ScheduleAvailability jcValue = getJobConditionFieldValue(jc, field);
            boolean rcValue = getRecruitConditionFieldValue(rc, field);

            // 점수 계산
            return calculateScoreForField(jcValue, rcValue, totalScore);
        } catch (Exception e) {
            throw new GlobalException(ErrorCode.ERROR_AT_CALCULATE_LOGIC);
        }
    }

    /**
     * JobCondition에서 해당 필드 값을 가져옵니다.
     */
    private ScheduleAvailability getJobConditionFieldValue(JobCondition jc, String field) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method jcMethod = JobCondition.class.getMethod("get" + field);
        return (ScheduleAvailability) jcMethod.invoke(jc);
    }

    /**
     * RecruitCondition에서 해당 필드 값을 가져옵니다.
     */
    private boolean getRecruitConditionFieldValue(RecruitCondition rc, String field) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method rcMethod = RecruitCondition.class.getMethod("is" + field);
        return (boolean) rcMethod.invoke(rc);
    }

    /**
     * 해당 필드의 조건에 맞춰 점수를 계산합니다.
     */
    private int calculateScoreForField(ScheduleAvailability jcValue, boolean rcValue, int totalScore) {
        if (jcValue == ScheduleAvailability.IMPOSSIBLE && rcValue) {
            totalScore -= 20; // 불가능한 조건에 맞을 때 20점 차감
        } else if (jcValue == ScheduleAvailability.NEGOTIABLE) {
            totalScore -= 2; // 조정 가능한 조건에 맞을 때 2점 차감
        }
        return totalScore;
    }

    /**
     * 시간 점수 계산 로직입니다.
     */
    private int calculateTimeScore(Map<Week, Long> rcDayTimeMap, JobCondition jc) {
        int totalScore = 0;
        int matchedDays = 0;

        // JC의 시간 마스크 가져오기
        long jcTimeMask = getTimeMask(jc.getStartTime(), jc.getEndTime());

        // RC의 시간 마스크와 비교하여 점수 계산
        for (Map.Entry<Week, Long> entry : rcDayTimeMap.entrySet()) {
            Week rcDay = entry.getKey();
            long rcTimeMask = entry.getValue();

            if (isDayMatched(jc, rcDay)) {
                matchedDays++;
                int score = calculateDayScore(jcTimeMask, rcTimeMask);
                totalScore += score;
            }
        }

        return calculateAverageScore(matchedDays, totalScore);
    }

    /**
     * 요일이 일치하면 True 아니면 False 반환합니다.
     */
    private boolean isDayMatched(JobCondition jc, Week rcDay) {
        return (jc.getDayOfWeek() & rcDay.getBitMask()) != 0;
    }

    /**
     * 하루의 시간 겹침 정도에 따른 점수 계산해서 반환합니다. (비트마스킹비교)
     */
    private int calculateDayScore(long jcTimeMask, long rcTimeMask) {
        long overlapped = jcTimeMask & rcTimeMask;
        int overlapCount = Long.bitCount(overlapped);
        int rcCount = Long.bitCount(rcTimeMask);

        return (int)((overlapCount / (double) rcCount) * 100);
    }

    /**
     * 최종 점수 계산 (평균 점수)
     */
    private int calculateAverageScore(int matchedDays, int totalScore) {
        return (matchedDays > 0) ? (totalScore / matchedDays) : 0;
    }

    /**
     * TimeMask로 변환하는 method 입니다.
     * startTime과 endTime 들어가면
     * 0000111111111100000
     * 처럼 시간대 체크된건 1로 표시되어서 반환됩니다.
     */
    private Long getTimeMask(Long startTime, Long endTime) {
        long mask = 0;
        for (Long i = startTime; i < endTime; i++) {
            mask |= (1L << i);
        }
        return mask;
    }
}
