package com.example.springserver.domain.center.converter;

import com.example.springserver.domain.center.dto.RecruitConditionOptionInfo;
import com.example.springserver.domain.center.dto.request.RecruitRequestDto.Request;
import com.example.springserver.domain.center.dto.request.RecruitRequestDto.TimeRequest;
import com.example.springserver.domain.center.dto.response.RecruitResponseDto.Response;
import com.example.springserver.domain.center.dto.response.RecruitResponseDto.TimeResponse;
import com.example.springserver.domain.center.entity.Elder;
import com.example.springserver.domain.center.entity.RecruitCondition;
import com.example.springserver.domain.center.entity.RecruitTime;
import com.example.springserver.domain.location.entity.Location;

import java.util.List;
import java.util.stream.Collectors;

public class RecruitConverter {

    public static RecruitCondition toRecruitCondition(Request request, Elder elder, Location location) {

        RecruitCondition recruitCondition = RecruitCondition.builder()
                .elder(elder)
                .careTypes(request.getCareTypes())
                .flexibleSchedule(request.getRecruitConditionOptionInfo().isFlexibleSchedule())
                .address(location.getAddress())
                .mealAssistance(request.getRecruitConditionOptionInfo().isMealAssistance())
                .toiletAssistance(request.getRecruitConditionOptionInfo().isToiletAssistance())
                .moveAssistance(request.getRecruitConditionOptionInfo().isMoveAssistance())
                .dailyLivingAssistance(request.getRecruitConditionOptionInfo().isDailyLivingAssistance())
                .desiredHourlyWage(request.getRecruitConditionOptionInfo().getDesiredHourlyWage())
                .selfFeeding(request.getRecruitConditionOptionInfo().isSelfFeeding())
                .mealPreparation(request.getRecruitConditionOptionInfo().isMealPreparation())
                .cookingAssistance(request.getRecruitConditionOptionInfo().isCookingAssistance())
                .enteralNutritionSupport(request.getRecruitConditionOptionInfo().isEnteralNutritionSupport())
                .selfToileting(request.getRecruitConditionOptionInfo().isSelfToileting())
                .occasionalToiletingAssist(request.getRecruitConditionOptionInfo().isOccasionalToiletingAssist())
                .diaperCare(request.getRecruitConditionOptionInfo().isDiaperCare())
                .catheterOrStomaCare(request.getRecruitConditionOptionInfo().isCatheterOrStomaCare())
                .independentMobility(request.getRecruitConditionOptionInfo().isIndependentMobility())
                .mobilityAssist(request.getRecruitConditionOptionInfo().isMobilityAssist())
                .wheelchairAssist(request.getRecruitConditionOptionInfo().isWheelchairAssist())
                .immobile(request.getRecruitConditionOptionInfo().isImmobile())
                .cleaningLaundryAssist(request.getRecruitConditionOptionInfo().isCleaningLaundryAssist())
                .bathingAssist(request.getRecruitConditionOptionInfo().isBathingAssist())
                .hospitalAccompaniment(request.getRecruitConditionOptionInfo().isHospitalAccompaniment())
                .exerciseSupport(request.getRecruitConditionOptionInfo().isExerciseSupport())
                .emotionalSupport(request.getRecruitConditionOptionInfo().isEmotionalSupport())
                .cognitiveStimulation(request.getRecruitConditionOptionInfo().isCognitiveStimulation())
                .detailRequiredService(request.getDetailRequiredService())
                .build();

        List<RecruitTime> recruitTimes = toRecruitTimeList(request.getRecruitTimes(), recruitCondition);
        recruitCondition.updateRecruitTimes(recruitTimes);
        recruitCondition.setRecruitLocation(location);

        return recruitCondition;
    }

    public static RecruitTime toRecruitTime(TimeRequest createReqTimeDto, RecruitCondition recruitcondition) {
        return RecruitTime.builder()
                .recruitCondition(recruitcondition)
                .startTime(createReqTimeDto.getStartTime())
                .endTime(createReqTimeDto.getEndTime())
                .dayOfWeek(createReqTimeDto.getDayOfWeek())
                .build();
    }

    public static List<RecruitTime> toRecruitTimeList(List<TimeRequest> recruitTimeDtoList, RecruitCondition recruitcondition) {
        return recruitTimeDtoList.stream()
                .map(dto -> toRecruitTime(dto, recruitcondition))
                .collect(Collectors.toList());
    }

    public static Response toConditionResponseDto(RecruitCondition recruitCondition) {
        return Response.builder()
                .recruitConditionId(recruitCondition.getRecruitConditionId())
                .elderId(recruitCondition.getElder().getElderId())
                .careTypes(recruitCondition.getCareTypes())
                .recruitLocation(recruitCondition.getRecruitLocation().getLocationId())
                .recruitTimes(toRecruitResponseTimeListDto(recruitCondition.getRecruitTimes()))
                .recruitConditionOptionInfo(
                        RecruitConditionOptionInfo.builder()
                                .mealAssistance(recruitCondition.isMealAssistance())
                                .toiletAssistance(recruitCondition.isToiletAssistance())
                                .moveAssistance(recruitCondition.isMoveAssistance())
                                .dailyLivingAssistance(recruitCondition.isDailyLivingAssistance())
                                .flexibleSchedule(recruitCondition.isFlexibleSchedule())
                                .desiredHourlyWage(recruitCondition.getDesiredHourlyWage())
                                .selfFeeding(recruitCondition.isSelfFeeding())
                                .mealPreparation(recruitCondition.isMealPreparation())
                                .cookingAssistance(recruitCondition.isCookingAssistance())
                                .enteralNutritionSupport(recruitCondition.isEnteralNutritionSupport())
                                .selfToileting(recruitCondition.isSelfToileting())
                                .occasionalToiletingAssist(recruitCondition.isOccasionalToiletingAssist())
                                .diaperCare(recruitCondition.isDiaperCare())
                                .catheterOrStomaCare(recruitCondition.isCatheterOrStomaCare())
                                .independentMobility(recruitCondition.isIndependentMobility())
                                .mobilityAssist(recruitCondition.isMobilityAssist())
                                .wheelchairAssist(recruitCondition.isWheelchairAssist())
                                .immobile(recruitCondition.isImmobile())
                                .cleaningLaundryAssist(recruitCondition.isCleaningLaundryAssist())
                                .bathingAssist(recruitCondition.isBathingAssist())
                                .hospitalAccompaniment(recruitCondition.isHospitalAccompaniment())
                                .exerciseSupport(recruitCondition.isExerciseSupport())
                                .emotionalSupport(recruitCondition.isEmotionalSupport())
                                .cognitiveStimulation(recruitCondition.isCognitiveStimulation())
                                .build()
                )
                .build();
    }

    // 모집 시간 convert
    public static TimeResponse toTimeResponseDto(RecruitTime recruitTime) {
        return TimeResponse.builder()
                .startTime(recruitTime.getStartTime())
                .endTime(recruitTime.getEndTime())
                .dayOfWeek(recruitTime.getDayOfWeek())
                .build();
    }

    public static List<Response> toListResponseDto(List<RecruitCondition> recruitConditionList) {
        return recruitConditionList.stream()
                .map(RecruitConverter::toConditionResponseDto)
                .collect(Collectors.toList());
    }

    public static List<TimeResponse> toRecruitResponseTimeListDto(List<RecruitTime> recruitTimeList) {
        return recruitTimeList.stream()
                .map(RecruitConverter::toTimeResponseDto)
                .collect(Collectors.toList());
    }
}
