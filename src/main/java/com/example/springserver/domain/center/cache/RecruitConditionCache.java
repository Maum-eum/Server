package com.example.springserver.domain.center.cache;

import com.example.springserver.domain.center.entity.enums.CareType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

import static com.example.springserver.domain.center.dto.response.RecruitResponseDto.ResponseTimeDto;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash(value = "recruitCondition", timeToLive = 60 * 60) // 1시간 TTL
public class RecruitConditionCache implements Serializable {

    @Id
    private Long id;

    private Long elderId;

    private Long locationId;

    private List<CareType> careTypes;

    private boolean flexibleSchedule;

    private String address;

    private List<ResponseTimeDto> recruitTimes;

    private boolean mealAssistance;

    private boolean toiletAssistance;

    private boolean moveAssistance;

    private boolean dailyLivingAssistance;

    private Integer desiredHourlyWage; // 희망 급여

    private boolean selfFeeding; // 스스로 식사 가능

    private boolean mealPreparation; // 식사 차려드리기

    private boolean cookingAssistance; // 요리 필요

    private boolean enteralNutritionSupport; // 경관식 보조

    private boolean selfToileting; // 스스로 배변 가능

    private boolean occasionalToiletingAssist; // 가끔 대소변 실수

    private boolean diaperCare; // 기저귀 케어 필요

    private boolean catheterOrStomaCare; // 유치도뇨/방광루/장루 관리

    private boolean independentMobility; // 스스로거동가능

    private boolean mobilityAssist; // 이동시 부축도움

    private boolean wheelchairAssist; // 휠체어 이동 보조

    private boolean immobile; // 거동 불가

    private boolean cleaningLaundryAssist; // 청소 빨래 보조

    private boolean bathingAssist; // 목욕 보조

    private boolean hospitalAccompaniment; // 병원 보조

    private boolean exerciseSupport; // 산책, 간단한 운동

    private boolean emotionalSupport; // 정서적 지원

    private boolean cognitiveStimulation; // 인지 자극 활동

    private String detailRequiredService;
}