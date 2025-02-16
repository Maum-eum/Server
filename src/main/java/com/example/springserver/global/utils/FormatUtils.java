package com.example.springserver.global.utils;

public class FormatUtils {
    private FormatUtils() {
    }
    public static Integer toIntegerDayOfWeek(String binaryString) {
        if (!binaryString.matches("^[01]{7}$")) {
            throw new IllegalArgumentException("Invalid binary string. Must be exactly 7 digits of 0s and 1s.");
        }
        return Integer.parseInt(binaryString, 2);
    }

    public static String toStringDayOfWeek(Integer week){
        return Integer.toBinaryString(week);
    }
}
