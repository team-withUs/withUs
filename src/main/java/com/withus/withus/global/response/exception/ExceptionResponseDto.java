package com.withus.withus.global.response.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExceptionResponseDto {

    private final HttpStatus status;

    private final String msg;

    public ExceptionResponseDto(ErrorCode e) {
        this.msg = e.getMsg();
        this.status = e.getStatus();
    }

    public ExceptionResponseDto(HttpStatus status, String msg) {
        this.status = status;
        this.msg = msg;
    }

}
