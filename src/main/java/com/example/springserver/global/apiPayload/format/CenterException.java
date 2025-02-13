package com.example.springserver.global.apiPayload.format;

public class CenterException extends GlobalException {

    public CenterException(ErrorCode errorCode) {
        super(errorCode);
    }
}
