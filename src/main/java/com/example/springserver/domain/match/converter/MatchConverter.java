package com.example.springserver.domain.match.converter;

import com.example.springserver.domain.caregiver.converter.CaregiverConverter;
import com.example.springserver.domain.caregiver.converter.JobConditionConverter;
import com.example.springserver.domain.center.converter.ElderConverter;
import com.example.springserver.domain.center.converter.RecruitConverter;
import com.example.springserver.domain.center.entity.Elder;
import com.example.springserver.domain.center.entity.RecruitCondition;
import com.example.springserver.domain.center.entity.RecruitTime;
import com.example.springserver.domain.center.entity.enums.CareType;
import com.example.springserver.domain.center.entity.enums.Inmate;
import com.example.springserver.domain.match.dto.response.MatchResponseDto;
import com.example.springserver.domain.match.dto.response.MatchResponseDto.*;
import com.example.springserver.domain.match.entity.Match;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MatchConverter {

    // 날짜를 포맷하는 메서드

    public static MatchedListRes toMatchedListRes(List<MatchedStatus> list) {
        return MatchedListRes.builder().list(list).build();
    }

    public static RequestsListRes toRequestListRes(List<MatchResponseDto.WorkRequest> list) {
        return RequestsListRes.builder()
                .list(list)
                .build();
    }

    public static MatchRecommendList toRecommendList(List<MatchedCaregiver> list) {
        return MatchRecommendList.builder().list(list).build();
    }

    public static ElderInfoDto.ElderInfoDtoBuilder toElderInfoDto(Elder elder, List<Inmate> inmateTypes) {
        return ElderInfoDto.builder()
                .name(elder.getName())
                .elderId(elder.getElderId())
                .img(elder.getImgUrl())
                .actsLikeChild(elder.isActsLikeChild())
                .gender(elder.getGender())
                .inmateTypes(inmateTypes)
                .isNormal(elder.isNormal())
                .birth(elder.getBirth())
                .hasDelusions(elder.isHasDelusions())
                .centerName(elder.getName())
                .hasAggressiveBehavior(elder.isHasAggressiveBehavior())
                .hasShortTermMemoryLoss(elder.isHasShortTermMemoryLoss())
                .rate(elder.getRate())
                .weight(elder.getWeight())
                .wandersOutside(elder.isWandersOutside());
    }

    public static RecruitCondRes.RecruitCondResBuilder toRecruitCondRes(RecruitCondition recruitCondition,List<CareType> careTypes,List<RecruitTime> recruitTimes) {
        return RecruitCondRes.builder()
                .bathingAssist(recruitCondition.isBathingAssist())
                .careTypes(recruitCondition.getCareTypes())
                .catheterOrStomaCare(recruitCondition.isCatheterOrStomaCare())
                .cleaningLaundryAssist(recruitCondition.isCleaningLaundryAssist())
                .cognitiveStimulation(recruitCondition.isCognitiveStimulation())
                .cookingAssistance(recruitCondition.isCookingAssistance())
                .detailRequiredService(recruitCondition.getDetailRequiredService())
                .dailyLivingAssistance(recruitCondition.isDailyLivingAssistance())
                .desiredHourlyWage(recruitCondition.getDesiredHourlyWage())
                .diaperCare(recruitCondition.isDiaperCare())
                .emotionalSupport(recruitCondition.isEmotionalSupport())
                .flexibleSchedule(recruitCondition.isFlexibleSchedule())
                .recruitLocation(recruitCondition.getRecruitLocation())
                .immobile(recruitCondition.isImmobile())
                .recruitTimes(recruitCondition.getRecruitTimes())
                .exerciseSupport(recruitCondition.isExerciseSupport())
                .enteralNutritionSupport(recruitCondition.isEnteralNutritionSupport())
                .hospitalAccompaniment(recruitCondition.isHospitalAccompaniment())
                .independentMobility(recruitCondition.isIndependentMobility())
                .mealAssistance(recruitCondition.isMealAssistance())
                .mealPreparation(recruitCondition.isMealPreparation())
                .mobilityAssist(recruitCondition.isMobilityAssist())
                .moveAssistance(recruitCondition.isMoveAssistance())
                .recruitConditionId(recruitCondition.getRecruitConditionId())
                .selfFeeding(recruitCondition.isSelfFeeding())
                .toiletAssistance(recruitCondition.isToiletAssistance())
                .selfToileting(recruitCondition.isSelfToileting())
                .wheelchairAssist(recruitCondition.isWheelchairAssist())
                .occasionalToiletingAssist(recruitCondition.isOccasionalToiletingAssist());
    }

    // entity -> dto 변환
    public static MatchDto toMatchDto(Match match) {
        return MatchDto.builder()
                .status(match.getStatus())
                .requirementCondition(
                        RecruitConverter.toConditionResponseDto(match.getRequirementCondition())
                        ) // recruitCondition -> dto 변환 필요
                .jobCondition(
                        JobConditionConverter.toJobConditionResponseDto(match.getJobCondition())
                        )
                .elderInfoDto(ElderConverter.toMatchElderDto(match.getRequirementCondition().getElder())) // elder -> dto 변환 필요
                .careGiverInfoDto(CaregiverConverter.toMatchCaregiverDto(match.getJobCondition().getCaregiver())) // careGiver -> dto 변환 필요
                .deletedAt(match.getDeletedAt())
                .version(match.getVersion())
                .build();
    }

    // entity List -> dto list로 변환
    public static List<MatchDto> toMatchDtoList(List<Match> matchList) {
        return matchList.stream()
                .map(MatchConverter::toMatchDto)
                .collect(Collectors.toList());
    }
}
