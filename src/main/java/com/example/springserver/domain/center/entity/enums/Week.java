package com.example.springserver.domain.center.entity.enums;

import java.util.Arrays;

//public enum Week {
//    MON,TUE,WED,THU,FRI,SAT,SUN
//}

public enum Week {
    SUN(1),
    MON(2),
    TUE(4),
    WED(8),
    THU(16),
    FRI(32),
    SAT(64);

    private final int bitMask;

    Week(int bitMask) {
        this.bitMask = bitMask;
    }

    public int getBitMask() {
        return bitMask;
    }

    public static Week fromBitMask(int bitMask) {
        return Arrays.stream(values())
                .filter(week -> week.bitMask == bitMask)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid bitMask: " + bitMask));
    }
}