package com.example.springserver.global.apiPayload.format;

public class CareException extends GlobalException {
    public CareException(ErrorCode errorCode) {
        super(errorCode);
    }
}
