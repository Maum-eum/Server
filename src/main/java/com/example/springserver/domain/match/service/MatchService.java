package com.example.springserver.domain.match.service;

import com.example.springserver.domain.caregiver.converter.CaregiverConverter;
import com.example.springserver.domain.caregiver.converter.JobConditionConverter;
import com.example.springserver.domain.caregiver.entity.Caregiver;
import com.example.springserver.domain.caregiver.entity.JobCondition;
import com.example.springserver.domain.caregiver.entity.enums.Sexual;
import com.example.springserver.domain.caregiver.repository.CaregiverRepository;
import com.example.springserver.domain.caregiver.repository.JobConditionRepository;
import com.example.springserver.domain.center.converter.ElderConverter;
import com.example.springserver.domain.center.converter.RecruitConverter;
import com.example.springserver.domain.center.entity.*;
import com.example.springserver.domain.center.entity.enums.RecruitStatus;
import com.example.springserver.domain.center.repository.AdminRepository;
import com.example.springserver.domain.center.repository.MatchRepository;
import com.example.springserver.domain.center.repository.RecruitConditionRepository;
import com.example.springserver.domain.match.dto.request.MatchRequestDto.RecruitReq;
import com.example.springserver.domain.match.dto.response.MatchResponseDto;
import com.example.springserver.domain.match.entity.Match;
import com.example.springserver.domain.match.entity.enums.MatchStatus;
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
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.springserver.domain.match.dto.response.MatchResponseDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchService {
    private final JobConditionRepository jobConditionRepository;
    private final CaregiverRepository caregiverRepository;
    private final RecruitConditionRepository recruitConditionRepository;
    private final MatchRepository matchRepository;
    private final AdminRepository adminRepository;

    public List<MatchedStatus> getCalenderList(CustomUserDetails user) {
        Caregiver caregiver = getValidCaregiver(user.getId());
        JobCondition jobCondition = jobConditionRepository.findByCaregiver(caregiver)
                .orElseThrow(() -> new GlobalException(ErrorCode.JOB_CONDITION_NOT_FOUND));

        List<Match> allByJobConditionWithStatus = matchRepository.findAllByJobConditionWithStatus(
                jobCondition, List.of(MatchStatus.MATCHED, MatchStatus.ENDED)
        );

        return allByJobConditionWithStatus.stream()
                .map(match -> {
                    Elder elder = match.getRecruitCondition().getElder();;
                    RecruitCondition rc = match.getRecruitCondition();
                    Center center = match.getCenter();

                    List<RecruitTime> recruitTimes = Optional.ofNullable(rc.getRecruitTimes())
                            .orElse(Collections.emptyList());

                    return MatchedStatus.builder()
                            .matchId(match.getId())
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

        Caregiver caregiver = getValidCaregiver(user.getId());

        if (request.getStatus() == RecruitStatus.ACCEPTED || request.getStatus() == RecruitStatus.TUNING) {
            originalMatch.setStatus(MatchStatus.TUNING);
            caregiver.changeEmploymentStatus(false);
        } else {
            originalMatch.setStatus(MatchStatus.DECLINED);
            originalMatch.setDeletedAt(LocalDateTime.now());
        }

        return "Status Updated";
    }

    public List<MatchResponseDto.WorkRequest> getRequests(CustomUserDetails user) {
        Caregiver cg = getValidCaregiver(user.getId());
        JobCondition jc = jobConditionRepository.findByCaregiver(cg)
                .orElseThrow(()-> new GlobalException(ErrorCode.JOB_CONDITION_NOT_FOUND));

        List<Match> allByJobConditionWithStatus = matchRepository.findAllByJobConditionWithStatus(jc, List.of(MatchStatus.WAITING,MatchStatus.TUNING));

        return toWorkRequestList(allByJobConditionWithStatus);
    }

    private List<MatchResponseDto.WorkRequest> toWorkRequestList(List<Match> allByJobConditionWithStatus) {
        return allByJobConditionWithStatus.stream()
                .map(match -> {
                    Elder elder = match.getRecruitCondition().getElder();;
                    Center center = elder.getCenter();
                    RecruitCondition rc = match.getRecruitCondition();
                    return MatchResponseDto.WorkRequest.builder()
                            .matchId(match.getId())
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

    @Transactional
    public MatchCreateDto createMatch(CustomUserDetails user, Long jobConditionId, Long recruitConditionId) {
        JobCondition jc = getValidJobCondition(jobConditionId);
        RecruitCondition rc = getValidRecruitCondition(recruitConditionId);
        Match match = Match.builder()
                .jobCondition(jc)
                .recruitCondition(rc)
                .status(MatchStatus.WAITING)
                        .build();
        matchRepository.save(match);
        return MatchCreateDto.builder().msg("요청이 완료되었습니다.").build();
    }

    public CareGiverInfo getRecommendResult(CustomUserDetails user, Long jobConditionId, Long recruitConditionId) {
        // get Valid Data
        JobCondition jobCondition = getValidJobCondition(jobConditionId);
        RecruitCondition recruitCondition = getValidRecruitCondition(recruitConditionId);
        Caregiver caregiver = getValidCaregiver(user.getId());
        Elder elder = recruitCondition.getElder();
        Admin admin = adminRepository.findByCenterId(elder.getCenter().getCenterId())
                .orElseThrow(()->new GlobalException(ErrorCode.ADMIN_NOT_FOUND));

        return CareGiverInfo.builder()
                .adminContact(admin.getConnect())
                .careGiverInfo(CaregiverConverter.infoResponseDto(caregiver))
                .elderInfoDto(ElderConverter.toResponseDto(elder))
                .jobCondRes(JobConditionConverter.toJobConditionResponseDTO(jobCondition))
                .recruitCondRes(RecruitConverter.toConditionResponseDto(recruitCondition))
                .build();
    }

    @Transactional
    public String answerToMatchRes(boolean status, Long jobConditionId, Long recruitConditionId) {
        Match byJcAndRC = matchRepository.findByJcAndRC(jobConditionId, recruitConditionId);
        JobCondition jobCondition = getValidJobCondition(jobConditionId);
        RecruitCondition recruitCondition = getValidRecruitCondition(recruitConditionId);
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

    private RecruitCondition getValidRecruitCondition(Long recruitConditionId) {
        return recruitConditionRepository.findById(recruitConditionId)
                .orElseThrow(() -> new GlobalException(ErrorCode.RECRUIT_NOT_FOUND));
    }

    private JobCondition getValidJobCondition(Long jobConditionId) {
        return jobConditionRepository.findById(jobConditionId)
                .orElseThrow(() ->new GlobalException(ErrorCode.JOB_CONDITION_NOT_FOUND));
    }

    private Caregiver getValidCaregiver(Long caregiverId) {
        return caregiverRepository.findById(caregiverId)
                .orElseThrow(()-> new GlobalException(ErrorCode.USER_NOT_FOUND));
    }
}
