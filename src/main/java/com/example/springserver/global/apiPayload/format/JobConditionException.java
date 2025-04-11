package com.example.springserver.global.apiPayload.format;

public class JobConditionException extends GlobalException {

    public JobConditionException(ErrorCode errorCode) {
        super(errorCode);
    }
}
