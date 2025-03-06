package com.example.springserver.domain.match.service;

import com.example.springserver.domain.caregiver.converter.CaregiverConverter;
import com.example.springserver.domain.caregiver.converter.JobConditionConverter;
import com.example.springserver.domain.caregiver.entity.Caregiver;
import com.example.springserver.domain.caregiver.entity.JobCondition;
import com.example.springserver.domain.caregiver.entity.enums.ScheduleAvailability;
import com.example.springserver.domain.caregiver.entity.enums.Sexual;
import com.example.springserver.domain.caregiver.repository.CaregiverRepository;
import com.example.springserver.domain.caregiver.repository.JobConditionRepository;
import com.example.springserver.domain.caregiver.service.CommonService;
import com.example.springserver.domain.center.converter.ElderConverter;
import com.example.springserver.domain.center.converter.RecruitConverter;
import com.example.springserver.domain.center.dto.response.RecruitResponseDto;
import com.example.springserver.domain.center.entity.*;
import com.example.springserver.domain.center.entity.enums.CareType;
import com.example.springserver.domain.center.entity.enums.Inmate;
import com.example.springserver.domain.center.entity.enums.Week;
import com.example.springserver.domain.center.repository.ElderRepository;
import com.example.springserver.domain.center.repository.RecruitCondRepository;
import com.example.springserver.domain.location.entity.Location;
import com.example.springserver.domain.match.dto.response.MatchResponseDto;
import com.example.springserver.domain.match.entity.enums.MatchStatus;
import com.example.springserver.domain.center.entity.Center;
import com.example.springserver.domain.center.entity.Elder;
import com.example.springserver.domain.center.entity.RecruitCondition;
import com.example.springserver.domain.center.entity.RecruitTime;
import com.example.springserver.domain.center.entity.enums.RecruitStatus;
import com.example.springserver.domain.center.entity.enums.Week;
import com.example.springserver.domain.center.repository.MatchRepository;
import com.example.springserver.domain.center.repository.RecruitCondRepository;
import com.example.springserver.domain.match.dto.request.MatchRequestDto.RecruitReq;
import com.example.springserver.domain.match.dto.response.MatchResponseDto;
import com.example.springserver.domain.match.entity.Match;
import com.example.springserver.domain.match.entity.enums.MatchStatus;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.GlobalException;
import com.example.springserver.global.security.util.CustomUserDetails;
import com.example.springserver.service.location.LocationService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.springserver.domain.match.dto.response.MatchResponseDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchService {

    private final CommonService commonService;
    private final JobConditionRepository jobConditionRepository;
    private final CaregiverRepository caregiverRepository;
    private final RecruitCondRepository recruitCondRepository;
    private final MatchRepository matchRepository;
    private final ElderRepository elderRepository;
    private final LocationService locationService;


    @Autowired
    private ModelMapper modelMapper;


    public List<MatchedStatus> getCalenderList(CustomUserDetails user) {
        Caregiver caregiver = commonService.getById(user);
        JobCondition jobCondition = jobConditionRepository.findByCaregiver(caregiver)
                .orElseThrow(() -> new GlobalException(ErrorCode.JOB_CONDITION_NOT_FOUND));

        List<Match> allByJobConditionWithStatus = matchRepository.findAllByJobConditionWithStatus(
                jobCondition, List.of(MatchStatus.MATCHED, MatchStatus.ENDED)
        );

        return allByJobConditionWithStatus.stream()
                .map(match -> {
                    Elder elder = match.getRequirementCondition().getElder();
                    RecruitCondition rc = match.getRequirementCondition();
                    Center center = match.getCenter();

                    List<RecruitTime> recruitTimes = Optional.ofNullable(rc.getRecruitTimes())
                            .orElse(Collections.emptyList());

                    return MatchedStatus.builder()
                            .centerId(center.getCenterId())
                            .recruitId(rc.getRecruitConditionId())
                            .elderId(elder.getElderId())
                            .elderName(elder.getName())
                            .mealAssistance(rc.isMealAssistance())
                            .dailyLivingAssistance(rc.isDailyLivingAssistance())
                            .toiletAssistance(rc.isToiletAssistance())
                            .moveAssistance(rc.isMoveAssistance())
                            .selfFeeding(rc.isSelfFeeding())
                            .mealPreparation(rc.isMealPreparation())
                            .cookingAssistance(rc.isCookingAssistance())
                            .enteralNutritionSupport(rc.isEnteralNutritionSupport())
                            .selfToileting(rc.isSelfToileting())
                            .occasionalToiletingAssist(rc.isOccasionalToiletingAssist())
                            .diaperCare(rc.isDiaperCare())
                            .catheterOrStomaCare(rc.isCatheterOrStomaCare())
                            .independentMobility(rc.isIndependentMobility())
                            .mobilityAssist(rc.isMobilityAssist())
                            .wheelchairAssist(rc.isWheelchairAssist())
                            .immobile(rc.isImmobile())
                            .cleaningLaundryAssist(rc.isCleaningLaundryAssist())
                            .bathingAssist(rc.isBathingAssist())
                            .hospitalAccompaniment(rc.isHospitalAccompaniment())
                            .exerciseSupport(rc.isExerciseSupport())
                            .emotionalSupport(rc.isEmotionalSupport())
                            .cognitiveStimulation(rc.isCognitiveStimulation())
                            .times(
                                    recruitTimes.stream()
                                            .map(rt -> WorkTimes.builder()
                                                    .dayOfWeek(rt.getDayOfWeek())
                                                    .startTime(rt.getStartTime())
                                                    .endTime(rt.getEndTime())
                                                    .build()
                                            )
                                            .collect(Collectors.toList())
                            )
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public String responseToRecruit(CustomUserDetails user, RecruitReq request) {
        Match originalMatch = matchRepository.findById(request.getMatchId())
                .orElseThrow(() -> new GlobalException(ErrorCode.MATCH_NOT_FOUND));

        Caregiver caregiver = commonService.getById(user);

        if (request.getStatus() == RecruitStatus.ACCEPTED || request.getStatus() == RecruitStatus.TUNING) {
            originalMatch.setStatus(MatchStatus.TUNING);
            caregiver.setEmploymentStatus(false);
        } else {
            originalMatch.setStatus(MatchStatus.DECLINED);
            originalMatch.setDeletedAt(LocalDateTime.now());
        }

        return "Status Updated";
    }

    public List<MatchResponseDto.WorkRequest> getRequests(CustomUserDetails user) {
        Caregiver cg = commonService.getById(user);
        JobCondition jc = jobConditionRepository.findByCaregiver(cg)
                .orElseThrow(()-> new GlobalException(ErrorCode.JOB_CONDITION_NOT_FOUND));

        List<Match> allByJobConditionWithStatus = matchRepository.findAllByJobConditionWithStatus(jc, List.of(MatchStatus.WAITING,MatchStatus.TUNING));

        return toWorkRequestList(allByJobConditionWithStatus);
    }

    private List<MatchResponseDto.WorkRequest> toWorkRequestList(List<Match> allByJobConditionWithStatus) {
        return allByJobConditionWithStatus.stream()
                .map(match -> {
                    Elder elder = match.getRequirementCondition().getElder();
                    Center center = elder.getCenter();
                    RecruitCondition rc = match.getRequirementCondition();
                    return MatchResponseDto.WorkRequest.builder()
                            .elderId(elder.getElderId())
                            .recruitConditionId(rc.getRecruitConditionId())
                            .imgUrl(elder.getImgUrl())
                            .status(match.getStatus())
                            .desiredHourlyWage(rc.getDesiredHourlyWage())
                            .rate(elder.getRate())
                            .careTypes(rc.getCareTypes())
                            .age(ChronoUnit.YEARS.between(elder.getBirth(), LocalDate.now()))
                            .sexual((elder.getGender() == 1) ? Sexual.MALE : Sexual.FEMALE)
                            .centerId(center.getCenterId())
                            .centerName(center.getCenterName())
                            .build();
                })
                .collect(Collectors.toList());
    }

    public List<MatchedCaregiver> getRecommendList(Long request) {

        List<JobCondition> recommendedList  = jobConditionRepository.findAllRecommendedListByElder(request);
        RecruitCondition recruitCondition = recruitCondRepository.findById(request)
                .orElseThrow(()-> new GlobalException(ErrorCode.RECRUIT_NOT_FOUND));

        // 매칭 점수 계산
        List<MatchedCaregiver> result = new ArrayList<>();
        for (JobCondition jobCondition : recommendedList) {
            int timeScore = calculateTimeScore(jobCondition,recruitCondition);
            int conditionScore = calculateConditionScore(jobCondition,recruitCondition);

            Caregiver caregiver = jobCondition.getCaregiver();
            MatchStatus st = matchRepository.findAllByJobConditionAndRecruitCondition(recruitCondition.getRecruitConditionId(),
                    jobCondition.getId());


            // 최종 점수 계산 (persent)
            int persent = (timeScore + conditionScore) / 2;

            // 결과 리스트 추가
            result.add(MatchedCaregiver.builder()
                            .jobConditionId(jobCondition.getId())
                            .caregiverName(caregiver.getName())
                            .imgUrl(caregiver.getImg())
                            .matchStatus(st == null ? MatchStatus.NONE : st )
                            .score(persent)
                    .build());
        }

        // 점수가 높은 순으로 정렬
        result.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));

        return result;
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

    private int calculateTimeScore(JobCondition jc, RecruitCondition rc) {
        List<RecruitTime> recruitTimes = rc.getRecruitTimes();
        int totalScore = 0;
        int matchedDays = 0;

        // JobCondition의 근무 시간을 비트마스크로 변환
        int jobTimeMask = getTimeMask(jc.getStartTime(), jc.getEndTime());
        int jobDayMask = jc.getDayOfWeek(); // ✅ 요양보호사의 근무 가능 요일 비트마스크

        for (RecruitTime rt : recruitTimes) {
            int recruitDayBit = getDayOfWeekBit(rt.getDayOfWeek()); // ✅ 어르신의 필요 근무 요일 비트

            // ✅ 요일이 겹치지 않으면 건너뛴다.
            if ((jobDayMask & recruitDayBit) == 0) {
                continue;
            }

            matchedDays++; // ✅ 요일이 겹치는 경우 카운트 증가

            // ✅ 요일이 일치할 때만 시간 비교 수행
            int recruitTimeMask = getTimeMask(rt.getStartTime(), rt.getEndTime());

            // 겹치는 시간 계산 (비트 연산)
            int overlappedTimeMask = jobTimeMask & recruitTimeMask;
            int overlapDuration = Integer.bitCount(overlappedTimeMask);
            int jobDuration = Integer.bitCount(jobTimeMask);

            // 비율 기반 점수 계산
            int score = (int) ((overlapDuration / (double) jobDuration) * 100);

            totalScore += score; // ✅ 요일별 점수를 누적
        }

        // ✅ 평균 점수 반환 (요일이 여러 개일 경우)
        return (matchedDays > 0) ? (totalScore / matchedDays) : 0;
    }

    private int getTimeMask(long startTime, long endTime) {
        int mask = 0;
        for (int i = (int)startTime; i < (int)endTime; i++) {
            mask |= (1 << i);
        }
        return mask;
    }

    private int getDayOfWeekBit(Week dayOfWeek) {
        return switch (dayOfWeek) {
            case SUN -> 1;
            case MON -> 2;
            case TUE -> 4;
            case WED -> 8;
            case THU -> 16;
            case FRI -> 32;
            case SAT -> 64;
        };
    }

    @Transactional
    public MatchCreateDto createMatch(CustomUserDetails user, Long jcid, Long rcid) {
        JobCondition jc = jobConditionRepository.findById(jcid)
                .orElseThrow(() -> new GlobalException(ErrorCode.JOB_CONDITION_NOT_FOUND));
        RecruitCondition rc = recruitCondRepository.findById(rcid)
                .orElseThrow(() -> new GlobalException(ErrorCode.RECRUIT_NOT_FOUND));
        Match match = Match.builder()
                .jobCondition(jc)
                .requirementCondition(rc)
                .status(MatchStatus.WAITING)
                        .build();
        matchRepository.save(match);
        return MatchCreateDto.builder().msg("요청이 완료되었습니다.").build();
    }

    public CareGiverInfo getRecommendResult(CustomUserDetails user, Long jc, Long rc) {
        JobCondition jobCondition = jobConditionRepository.findById(jc)
                .orElseThrow(() -> new GlobalException(ErrorCode.JOB_CONDITION_NOT_FOUND));
        RecruitCondition recruitCondition = recruitCondRepository.findById(rc)
                .orElseThrow(() -> new GlobalException(ErrorCode.RECRUIT_NOT_FOUND));
        Caregiver caregiver = caregiverRepository.findById(jobCondition.getId())
                .orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));
        Elder elder = recruitCondition.getElder();

        return CareGiverInfo.builder()
                .careGiverInfo(CaregiverConverter.infoResponseDto(caregiver))
                .elderInfoDto(ElderConverter.toResponseDto(elder))
                .jobCondRes(JobConditionConverter.tojobConditionResponseDTO(jobCondition))
                .recruitCondRes(RecruitConverter.toConditionResponseDto(recruitCondition))
                .build();
    }

    @Transactional
    public String answerToMatchRes(boolean status,Long jc, Long rc) {
        Match byJcAndRC = matchRepository.findByJcAndRC(jc, rc);
        JobCondition jobCondition = jobConditionRepository.findById(jc)
                .orElseThrow(() ->new GlobalException(ErrorCode.JOB_CONDITION_NOT_FOUND));
        RecruitCondition recruitCondition = recruitCondRepository.findById(rc)
                .orElseThrow(() -> new GlobalException(ErrorCode.RECRUIT_NOT_FOUND));
        if(!status) {
            byJcAndRC.setStatus(MatchStatus.DECLINED);
            byJcAndRC.setDeletedAt(LocalDateTime.now());
        }else {
            if(Objects.equals(jobCondition.getDesiredHourlyWage(),recruitCondition.getDesiredHourlyWage()))
                byJcAndRC.setStatus(MatchStatus.MATCHED);
            else
                throw new GlobalException(ErrorCode.MONEY_NOT_MATCHED);
        }
        return "매칭 상태 변경 완료";
    }

    public List<Match> getCenterMatchingList(Long centerId) {

        return matchRepository.findByCenterId(centerId);
    }
}
