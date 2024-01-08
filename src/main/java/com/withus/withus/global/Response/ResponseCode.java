package com.withus.withus.global.Response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    /* 200 OK */
    OK(200, "요청 성공"),
    GET_PROFILE(200,"프로필 가져오기 성공");

    /* 201 CREATED */


    private final int httpStatus;
    private final String message;

}
