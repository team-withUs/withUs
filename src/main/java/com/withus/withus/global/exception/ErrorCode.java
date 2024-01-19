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

    NOT_CHANGED_PASSWORD(BAD_REQUEST,"이전 비밀번호와 다른 비밀번호여야 합니다."),

    FAILED_UPLOAD_IMAGE(BAD_REQUEST,"이미지 업로드에 실패했습니다."),

    OVER_FILE_SIZE(BAD_REQUEST, "파일의 크기가 50MB보다 큽니다."),

    CLUB_EXIST_REPORT(BAD_REQUEST,"이미 해당 클럽을 신고하셨습니다."),

    NOTICE_EXIST_REPORT(BAD_REQUEST,"이미 해당 게시판을 신고하셨습니다."),

    COMMENT_EXIST_REPORT(BAD_REQUEST, "이미 해당 댓글을 신고하셨습니다."),

    REJECT_SEND_CHAT_SELF(BAD_REQUEST, "자기 자신에게 채팅을 할 수 없습니다."),

    LOGOUT_USER(FORBIDDEN, "로그아웃 유저입니다. 다시 로그인 해주세요."),

    /* 401 UNAUTHORIZED */
    ACCESS_DENIED(UNAUTHORIZED, "유효하지 못한 토큰입니다."),

    EXPIRED_TOKEN(UNAUTHORIZED, "만료된 토큰입니다."),

    /* 403 FORBIDDEN */
    YOUR_NOT_COME_IN(FORBIDDEN, "권한이 없습니다"), // 포괄적인 Forbidden 코드

    YOUR_NOT_THE_AUTHOR(FORBIDDEN, "작성자가 아닙니다."),

    /* 404 NOT_FOUND */
    NOT_EXIST_REFRESH_TOKEN(NOT_FOUND, "리프레시토큰이 존재하지 않습니다."),

    NOT_EXIST_ACCESS_TOKEN(NOT_FOUND, "엑세스토큰이 존재하지 않슴니다."),

    NOT_FOUND_CLUB(NOT_FOUND, "존재하지 않는 클럽입니다."),

    NOT_FOUND_MEMBER(NOT_FOUND, "해당 멤버를 찾을 수 없습니다"),

    NOT_FOUND_NOTICE(NOT_FOUND,"해당 Notice을 찾을 수없습니다."),

    NOT_FOUND_CATEGORY(NOT_FOUND,"해당 카테고리을 찾을 수없습니다."),

    NOT_FOUND_CLUB_MEMBER_EXIST(NOT_FOUND,"클럽멤버가 아닙니다"),

    NOT_FOUND_CLUB_TITLE_EXIST(NOT_FOUND,"클럽제목이 없습니다."),

    NOT_FOUND_CHATROOM(NOT_FOUND, "해당 채팅방을 찾을 수 없습니다."),

    NOT_FOUND_COMMENT(NOT_FOUND, "해당 댓글을 찾을 수 없습니다."),


    /* 409 CONFLICT */
    DUPLICATE_MEMBER(CONFLICT, "이미 가입한 멤버 입니다."),

    DUPLICATE_USERNAME(CONFLICT, "중복된 유저이름 입니다"),

    DUPLICATE_EMAIL(CONFLICT, "중복된 Email 입니다"),

    DUPLICATE_REPORT(CONFLICT, "이미 신고한 대상입니다."),

    DELETED_MEMBER(CONFLICT, "이미 탈퇴한 회원입니다."),



    /* 그 외 */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");

    private final HttpStatus status;
    private final String msg;
}
