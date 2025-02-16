package com.example.springserver.domain.center.converter;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.GlobalException;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;

public class TimeConverter {

    public static LocalTime convertToDateTime(long timeIndex) {
        if (timeIndex < 0 || timeIndex > 48) {
            throw new GlobalException(ErrorCode.TIME_LIMIT_OUT_OF_RANGE);
        }

        return LocalTime.MIN.plusMinutes(timeIndex*30);
    }
}
