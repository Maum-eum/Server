package com.example.springserver.domain.match.converter;

import com.example.springserver.domain.center.entity.Elder;
import com.example.springserver.domain.center.entity.RecruitCondition;
import com.example.springserver.domain.center.entity.RecruitTime;
import com.example.springserver.domain.center.entity.enums.CareType;
import com.example.springserver.domain.center.entity.enums.Inmate;
import com.example.springserver.domain.match.dto.response.MatchResponseDto;
import com.example.springserver.domain.match.dto.response.MatchResponseDto.*;

import com.example.springserver.domain.match.dto.response.MatchResponseDto.RequestsListRes;
import java.util.List;

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

}
