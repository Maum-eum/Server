package com.example.springserver.global.apiPayload.format;

public class CacheException extends GlobalException {

    public CacheException(ErrorCode errorCode) {
        super(errorCode);
    }
}
