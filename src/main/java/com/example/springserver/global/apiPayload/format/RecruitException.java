package com.example.springserver.global.apiPayload.format;

public class RecruitException extends GlobalException {

    public RecruitException(ErrorCode errorCode) {
        super(errorCode);
    }
}
