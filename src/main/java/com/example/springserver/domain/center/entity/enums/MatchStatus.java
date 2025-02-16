package com.example.springserver.domain.center.entity.enums;

public enum MatchStatus {
    BEFORE,   //응답대기중
    MATCHED,  //진행중
    TUNING,   //조율중
    DECLINED, //거절됨
    ENDED     //서비스종료
}
