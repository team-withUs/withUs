package com.withus.withus.global.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_VALUE(BAD_REQUEST, "잘못된 입력값입니다."),

    NOT_FOUND_MEMBER(NOT_FOUND, "존재하지 않는 회원입니다.");

    private final HttpStatus status;
    private final String msg;
}
