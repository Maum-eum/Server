package com.example.springserver.global.validation.validator;

import com.example.springserver.domain.center.dto.request.RecruitRequestDto;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.RecruitException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class RecruitLaborLawValidator {

    private static final int MINIMUM_WAGE = 10030;
    private static final int DAILY_MAXIMUM_LABOR_TIME = 8;

    public void validateRecruitRequest(RecruitRequestDto.RequestDto requestDto) {
        if (requestDto.getRecruitTimes() == null || requestDto.getRecruitTimes().isEmpty()) {
            throw new RecruitException(ErrorCode.RECRUIT_TIME_INVALID);
        }

        for (RecruitRequestDto.RequestTimeDto timeDto : requestDto.getRecruitTimes()) {
            long dailyHour = calculateWorkingHours(timeDto);

            validateMinimumWage(requestDto.getDesiredHourlyWage());
            validateWorkingHours(dailyHour);
        }
    }

    private long calculateWorkingHours(RecruitRequestDto.RequestTimeDto dto) {
        return Duration.between(
                convertToLocalTime(dto.getStartTime()),
                convertToLocalTime(dto.getEndTime())
        ).toHours();
    }

    private LocalTime convertToLocalTime(Long time) {
        return LocalTime.MIN.plusMinutes(time * 30);
    }

    public void validateMinimumWage(int wagePerHour) {
        if (wagePerHour < MINIMUM_WAGE) {
            throw new RecruitException(ErrorCode.RECRUIT_LABOR_WAGE_INVALID);
        }
    }

    public void validateWorkingHours(long hoursPerDay) {
        if (hoursPerDay > DAILY_MAXIMUM_LABOR_TIME) {
            throw new RecruitException(ErrorCode.RECRUIT_DAILY_LABOR_TIME_INVALID);
        }
    }
}