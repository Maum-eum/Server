package com.example.springserver.domain.center.converter;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.GlobalException;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;

public class TimeConverter {

    public static LocalDateTime convertToDateTime(long timeIndex) {
        if (timeIndex < 0 || timeIndex > 48) {
            throw new GlobalException(ErrorCode.TIME_LIMIT_OUT_OF_RANGE);
        }

        LocalDate today = LocalDate.now();

        int hours = (int) (timeIndex / 2);
        int minutes = (int) ((timeIndex % 2) * 30);

        return LocalDateTime.of(today, LocalTime.of(hours, minutes));
    }
}
