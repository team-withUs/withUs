package com.withus.withus.member.controller;

import com.withus.withus.club.dto.ClubResponseDto;
import com.withus.withus.global.annotation.AuthMember;
import com.withus.withus.global.response.CommonResponse;
import com.withus.withus.global.response.ResponseCode;
import com.withus.withus.member.dto.*;
import com.withus.withus.member.entity.Member;
import com.withus.withus.member.service.MemberServiceImpl;
import com.withus.withus.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List ;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;


@RestController
@AllArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

  private final MemberServiceImpl memberService;

  @PostMapping("/signup/email")
  public ResponseEntity<CommonResponse<EmailRequestDto>> authenticationEmail (
      @Valid @RequestBody EmailRequestDto emailRequestDto
  ) {
    memberService.sendAuthCodeToEmail(emailRequestDto);
    return ResponseEntity.status(ResponseCode.SEND_MAIL.getHttpStatus()).body(CommonResponse.of(ResponseCode.SEND_MAIL, emailRequestDto));
  }

  @PostMapping("/signup")
  public ResponseEntity<CommonResponse<String>> signupMember (
      @Valid @RequestBody SignupRequestDto signupRequestDto
  ) {

    memberService.signup(signupRequestDto);
    return ResponseEntity.status(ResponseCode.SIGNUP.getHttpStatus()).body(CommonResponse.of(ResponseCode.SIGNUP, ""));
  }

  @GetMapping("/{memberId}")
  public ResponseEntity<CommonResponse<MemberResponseDto>> getMember(
      @PathVariable("memberId") Long memberId
  ) {
    MemberResponseDto memberResponseDto = memberService.getMember(memberId);

    return ResponseEntity
        .status(ResponseCode.GET_PROFILE.getHttpStatus())
        .body(CommonResponse.of(ResponseCode.GET_PROFILE, memberResponseDto));
  }
  // 추가
  @GetMapping("email/{email}")
  public ResponseEntity<CommonResponse<MemberResponseDto>> getMemberEmail(
          @PathVariable("email") String email
  ){
    MemberResponseDto memberResponseDto = memberService.getMemberEmail(email);
    return ResponseEntity
            .status(ResponseCode.OK.getHttpStatus())
            .body(CommonResponse.of(ResponseCode.OK, memberResponseDto));
  }

  @PatchMapping("/{memberId}")
  public ResponseEntity<CommonResponse<MemberResponseDto>> updateMember(
      @PathVariable("memberId") Long memberId,
      @AuthMember Member member,
      @Valid @ModelAttribute UpdateRequestDto updateRequestDto
      ) {
    MemberResponseDto memberResponseDto = memberService.updateMember(
        memberId,
        updateRequestDto,
        member
    );

    return ResponseEntity.status(ResponseCode.UPDATE_PROFILE.getHttpStatus())
        .body(CommonResponse.of(ResponseCode.UPDATE_PROFILE, memberResponseDto));
  }

  @PatchMapping("/{memberId}/updatePassword")
  public ResponseEntity<CommonResponse<MemberResponseDto>> updatePassword(
      @PathVariable("memberId") Long memberId,
      @AuthMember Member member,
      @Valid @RequestBody PasswordRequestDto passwordRequestDto
  ) {
    MemberResponseDto memberResponseDto = memberService.updatePassword(
      memberId,
      passwordRequestDto,
      member
    );

    return ResponseEntity.status(ResponseCode.UPDATE_PASSWORD.getHttpStatus())
        .body(CommonResponse.of(ResponseCode.UPDATE_PASSWORD, memberResponseDto));
  }

  @DeleteMapping("/{memberId}")
  public ResponseEntity<CommonResponse<String>> deleteMember(
      @PathVariable("memberId") Long memberId,
      @AuthMember Member member
  ) {
      memberService.deleteMember(memberId, member);
      return ResponseEntity
          .status(ResponseCode.RESIGN_MEMBER.getHttpStatus())
          .body(CommonResponse.of(ResponseCode.RESIGN_MEMBER,""));
  }

  @PostMapping("/{memberId}/report")
  public ResponseEntity<CommonResponse<String>> reportMember(
      @PathVariable("memberId") Long memberId,
      @RequestBody ReportRequestDto reportRequestDto,
      @AuthMember Member member
  ) {
    memberService.reportMember(memberId,reportRequestDto,member);
    return ResponseEntity
        .status(ResponseCode.INVITE_MEMBER.getHttpStatus())
        .body(CommonResponse.of(ResponseCode.SUCCESS_MEMBER_REPORT,""));
  }

  @GetMapping("/club")
  public ResponseEntity<CommonResponse<Page<ClubResponseDto>>> getMyClubList(
      Pageable pageable,
      @AuthMember Member member
  ) {
    Page<ClubResponseDto> clubResponseDtoList = memberService.getMyClubList(pageable,member);

    return ResponseEntity
        .status(ResponseCode.GET_MY_CLUBLIST.getHttpStatus())
        .body(CommonResponse.of(ResponseCode.GET_MY_CLUBLIST,clubResponseDtoList));
  }

  @GetMapping("/myHostingClub")
  public ResponseEntity<CommonResponse<Page<ClubResponseDto>>> getMyHostingClubList(
      Pageable pageable,
      @AuthMember Member member
  ) {
    Page<ClubResponseDto> clubResponseDtoPage = memberService.getMyHostingClubList(
        pageable,
        member.getId()
    );

    return ResponseEntity
        .status(ResponseCode.GET_MY_CLUBLIST.getHttpStatus())
        .body(CommonResponse.of(ResponseCode.GET_MY_CLUBLIST,clubResponseDtoPage));
  }

  @PostMapping("/{memberId}/club/{clubId}")
  public ResponseEntity<CommonResponse<String>> inviteMember(
      @PathVariable("memberId") Long memberId,
      @PathVariable("clubId") Long clubId,
      @AuthMember Member member
  ) {
    memberService.inviteMember(memberId,clubId,member);

    return ResponseEntity.status(ResponseCode.INVITE_MEMBER.getHttpStatus())
        .body(CommonResponse.of(ResponseCode.INVITE_MEMBER,""));
  }

  @PostMapping("/passwordCheck")
  public ResponseEntity<CommonResponse<String>> passwordCheck(
      @AuthMember Member member,
      @RequestBody PasswordRequestDto passwordRequestDto
  ){
    if(memberService.passwordCheck(member,passwordRequestDto)){
      return ResponseEntity.status(ResponseCode.MATCHED_PASSWORD.getHttpStatus())
          .body(CommonResponse.of(ResponseCode.MATCHED_PASSWORD,"일치"));
    }else{
      return ResponseEntity.status(ResponseCode.MATCHED_PASSWORD.getHttpStatus())
          .body(CommonResponse.of(ResponseCode.MATCHED_PASSWORD,"불일치"));
    }
  }
}
