package com.example.springserver.global.validation.validator;

import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.RecruitException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecruitLaborLawValidator { // 2025년 근로기준법 기준

    private static final int MINIMUM_WAGE = 10030;
    private static final int DAILY_MAXIMUM_LABOR_TIME = 8;
    private static final int MONTHLY_MAXIMUM_LABOR_TIME = 40;

    // 최저임금 검증: 최저 임금 10,030원
    public void validateMinimumWage(int wagePerHour) {
        if (wagePerHour < MINIMUM_WAGE) {
            throw new RecruitException(ErrorCode.RECRUIT_LABOR_WAGE_INVALID);
        }
    }

    // 근로 시간 검증: 1일 최대 8시간 근무
    public void validateWorkingHours(int hoursPerDay) {
        if (hoursPerDay > DAILY_MAXIMUM_LABOR_TIME) {
            throw new RecruitException(ErrorCode.RECRUIT_DAILY_LABOR_TIME_INVALID);
        }
    }

    // 초과 근무 검증: 최대 주 40시간 근무 가능
    public void validateOvertimeRules(int hoursPerWeek) {
        if (hoursPerWeek > MONTHLY_MAXIMUM_LABOR_TIME) {
            throw new RecruitException(ErrorCode.RECRUIT_MONTHLY_LABOR_TIME_INVALID);
        }
    }
}