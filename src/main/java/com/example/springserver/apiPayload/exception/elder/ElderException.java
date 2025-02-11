package com.example.springserver.apiPayload.exception.elder;

import com.example.springserver.apiPayload.code.BaseCode;
import com.example.springserver.apiPayload.exception.GeneralException;

public class ElderException extends GeneralException {

    public ElderException(BaseCode code) {
        super(code);
    }
}
