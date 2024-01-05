package com.withus.withus.global.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_VALUE(BAD_REQUEST, "잘못된 입력값입니다.");

    private final HttpStatus status;
    private final String msg;
}
