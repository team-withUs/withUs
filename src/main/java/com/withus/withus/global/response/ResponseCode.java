package com.withus.withus.global.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    /* 200 OK */
    /* MEMBER */
    OK(200, "요청 성공"),

    GET_PROFILE(200,"프로필 가져오기 성공"),

    SEND_MAIL(200, "이메일이 발송되었습니다. 이메일을 확인해주세요."),

    LOGIN(200, "로그인 성공"),

    SUCCESS_REISSUANCETOKEN(200, "토큰이 재발급되었습니다. 다시 시도해주세요."),

    LOGOUT(200, "로그아웃 되었습니다."),

    UPDATE_PROFILE(200, "프로필 수정 성공"),

    RESIGN_MEMBER(200, "회원탈퇴 성공"),

    GET_MY_CLUBLIST(200, "내 클럽 불러오기 성공"),

    SUCCESS_MEMBER_REPORT(200, "Member 신고 성공"),
  
    /* NOTICE */
    SUCCESS_NOTICE_UPDATE(200,"Notice 수정 성공"),
  
    SUCCESS_NOTICE_GET(200,"Notice 선택조회 성공"),
  
    SUCCESS_NOTICE_DELETE(200,"Notice 삭제 성공"),
  
    SUCCESS_NOTICE_REPORT(200,"Notice 신고 성공"),

    SUCCESS_NOTICE_GETS(200,"Notice 전체조회(페이징) 성공"),

    INVITE_MEMBER(200, "초대 성공"),

    /* 201 CREATED */
    SIGNUP(201, "회원가입 성공"),

    SUCCESS_NOTICE_CREATE(201,"Notice 생성 성공"),

    SUCCESS_CLUB_CREATE(201,"Club 생성 성공"),

    SUCCESS_COMMENT_CREATE(201, "Comment 생성 성공"),

    SUCCESS_CLUB_REPORT(201, "Club 신고 성공"),

    SUCCESS_CLUB_GET(200,"Club 조회 성공"),

    /* 201 UPDATE*/
    SUCCESS_CLUB_UPDATE(201,"Club 업데이트 성공"),

    SUCCESS_COMMENT_UPDATE(201, "Comment 업데이트 성공"),

    /* 201 DELETE*/
    SUCCESS_CLUB_DELETE(201,"Club 삭제 성공");


    private final int httpStatus;
    private final String message;

}
