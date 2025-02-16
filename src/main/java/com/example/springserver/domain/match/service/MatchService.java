package com.example.springserver.domain.match.service;

import com.example.springserver.domain.caregiver.dto.request.CaregiverRequestDto.*;
import com.example.springserver.domain.caregiver.dto.response.CaregiverResponseDto.*;
import com.example.springserver.domain.caregiver.dto.response.CaregiverResponseDto.WorkTimes;
import com.example.springserver.domain.caregiver.entity.Caregiver;
import com.example.springserver.domain.caregiver.entity.JobCondition;
import com.example.springserver.domain.caregiver.entity.enums.Sexual;
import com.example.springserver.domain.caregiver.repository.JobConditionRepository;
import com.example.springserver.domain.caregiver.service.CommonService;
import com.example.springserver.domain.center.entity.*;
import com.example.springserver.domain.center.entity.enums.MatchStatus;
import com.example.springserver.domain.center.entity.enums.RecruitStatus;
import com.example.springserver.domain.center.repository.MatchRepository;
import com.example.springserver.domain.match.dto.request.MatchRequestDto;
import com.example.springserver.domain.match.dto.request.MatchRequestDto.RecruitReq;
import com.example.springserver.domain.match.entity.Match;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.GlobalException;
import com.example.springserver.global.security.util.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchService {

    private final CommonService commonService;
    private final JobConditionRepository jobConditionRepository;
    private final MatchRepository matchRepository;

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

                    List<RecruitTime> recruitTimes = Optional.ofNullable(rc.getRecruitTimes())
                            .orElse(Collections.emptyList());

                    return MatchedStatus.builder()
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

    public List<WorkRequest> getRequests(CustomUserDetails user) {
        Caregiver cg = commonService.getById(user);
        JobCondition jc = jobConditionRepository.findByCaregiver(cg)
                .orElseThrow(()-> new GlobalException(ErrorCode.JOB_CONDITION_NOT_FOUND));

        List<Match> allByJobConditionWithStatus = matchRepository.findAllByJobConditionWithStatus(jc, List.of(MatchStatus.BEFORE,MatchStatus.TUNING));

        return toWorkRequestList(allByJobConditionWithStatus);
    }

    private List<WorkRequest> toWorkRequestList(List<Match> allByJobConditionWithStatus) {
        return allByJobConditionWithStatus.stream()
                .map(match -> {
                    Elder elder = match.getRequirementCondition().getElder();
                    Center center = elder.getCenter();
                    RecruitCondition rc = match.getRequirementCondition();
                    return WorkRequest.builder()
                            .elderId(elder.getElderId())
                            .recruitConditionId(rc.getRecruitConditionId())
                            .imgUrl(elder.getImgUrl())
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



}
