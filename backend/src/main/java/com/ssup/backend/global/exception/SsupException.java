package com.ssup.backend.global.exception;

import lombok.Getter;

public class SsupException extends RuntimeException{

    @Getter
    private ErrorCode errorCode;

    public SsupException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}