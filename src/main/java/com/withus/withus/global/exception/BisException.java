package com.withus.withus.global.exception;

import lombok.Getter;

@Getter
public class BisException extends RuntimeException {

    private final ErrorCode errorCode;

    public BisException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.errorCode = errorCode;
    }
}