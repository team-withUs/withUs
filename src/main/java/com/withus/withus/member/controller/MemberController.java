package com.withus.withus.member.controller;

import com.withus.withus.global.response.CommonResponse;
import com.withus.withus.global.response.ResponseCode;
import com.withus.withus.global.security.UserDetailsImpl;
import com.withus.withus.member.dto.EmailRequestDto;
import com.withus.withus.member.dto.MemberResponseDto;
import com.withus.withus.member.dto.SignupRequestDto;
import com.withus.withus.member.dto.UpdateRequestDto;
import com.withus.withus.member.service.MemberServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

  private final MemberServiceImpl memberService;


  @PostMapping("/signup/email")
  public ResponseEntity<CommonResponse> authenticationEmail (
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

  @PatchMapping("/{memberId}")
  public ResponseEntity<CommonResponse<MemberResponseDto>> updateMember(
      @PathVariable("memberId") Long memberId,
      @Valid @RequestBody UpdateRequestDto updateRequestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    MemberResponseDto memberResponseDto = memberService.updateMember(
        memberId,
        updateRequestDto,
        userDetails.getMember()
    );

    return ResponseEntity.status(ResponseCode.UPDATE_PROFILE.getHttpStatus())
        .body(CommonResponse.of(ResponseCode.UPDATE_PROFILE, memberResponseDto));
  }

  @DeleteMapping("/{memberId}")
  public ResponseEntity<CommonResponse> deleteMember(
      @PathVariable("memberId") Long memberId,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
      memberService.deleteMember(memberId, userDetails.getMember());

      return ResponseEntity
          .status(ResponseCode.RESIGN_MEMBER.getHttpStatus())
          .body(CommonResponse.of(ResponseCode.RESIGN_MEMBER,""));
  }

  @PatchMapping("/report/{memberId}")
  public ResponseEntity<CommonResponse> reportMember(
      @PathVariable("memberId") Long memberId,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    memberService.reportMember(memberId,userDetails.getMember());
    return ResponseEntity
        .status(ResponseCode.INVITE_MEMBER.getHttpStatus())
        .body(CommonResponse.of(ResponseCode.INVITE_MEMBER,""null""));
  }

}
