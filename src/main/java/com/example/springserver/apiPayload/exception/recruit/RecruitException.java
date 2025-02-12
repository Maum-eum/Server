package com.example.springserver.apiPayload.exception.recruit;

import com.example.springserver.apiPayload.code.BaseCode;
import com.example.springserver.apiPayload.exception.GeneralException;

public class RecruitException extends GeneralException {

    public RecruitException(BaseCode baseCode) {
        super(baseCode);
    }
}
