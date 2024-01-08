package com.withus.withus.global.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    /* 200 OK */
    /* MEMBER */
    OK(200, "요청 성공"),

    SEND_MAIL(200, "이메일이 발송되었습니다. 이메일을 확인해주세요."),

    LOGIN(200, "로그인 성공"),

    SUCCESS_REISSUANCETOKEN(200, "토큰이 재발급되었습니다. 다시 시도해주세요."),

    LOGOUT(200, "로그아웃 되었습니다."),

    /* 201 CREATED */
    SIGNUP(201, "회원가입 성공"),
    SUCCESS_NOTICE_CREATE(201,"Notice 생성 성공");

    private final int httpStatus;
    private final String message;

}
