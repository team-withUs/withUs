package com.withus.withus.global.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST */
    INVALID_VALUE(BAD_REQUEST, "잘못된 입력값입니다."),

    NOT_MATCH_PASSWORD(BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    NOT_MATCH_PASSWORD_CHECK(BAD_REQUEST, "비밀번호와 비밀번호 확인이 일치하지 않습니다."),

    NOT_MATCH_AUTHCODE(BAD_REQUEST, "인증번호가 일치하지 않습니다."),

    /* 401 UNAUTHORIZED */
    ACCESS_DENIED(UNAUTHORIZED, "유효하지 못한 토큰입니다."),

    LOGOUT_USER(UNAUTHORIZED, "로그아웃한 유저입니다. 다시 로그인해주세요."),

    EXPIRED_TOKEN(UNAUTHORIZED, "만료된 토큰입니다. 다시로그인하세요."),

    NOT_EXIST_REFRESH_TOKEN(UNAUTHORIZED, "리프레시토큰이 존재하지 않습니다."),

    NOT_EXIST_ACCESS_TOKEN(UNAUTHORIZED, "엑세스토큰이 존재하지 않슴니다."),

    /* 403 FORBIDDEN */
    YOUR_NOT_COME_IN(FORBIDDEN, "권한이 없습니다"), // 포괄적인 Forbidden 코드

    /* 404 NOT_FOUND */
    NOT_FOUND_MEMBER(NOT_FOUND, "해당 멤버를 찾을 수 없습니다"),

    /* 409 CONFLICT */
    DUPLICATE_MEMBER(CONFLICT, "이미 가입한 멤버 입니다."),

    DUPLICATE_USERNAME(CONFLICT, "중복된 유저이름 입니다"),

    DUPLICATE_EMAIL(CONFLICT, "중복된 Email 입니다"),

    /* 그 외 */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");

    private final HttpStatus status;
    private final String msg;
}
