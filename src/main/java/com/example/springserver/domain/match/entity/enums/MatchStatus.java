package com.example.springserver.domain.match.entity.enums;

public enum MatchStatus {
    WAITING,   //응답대기중
    MATCHED,  //진행중
    TUNING,   //조율중
    DECLINED, //거절됨
    NONE, ENDED     //서비스종료
}
