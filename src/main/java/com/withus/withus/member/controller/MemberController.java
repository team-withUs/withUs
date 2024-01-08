package com.withus.withus.member.controller;

import com.withus.withus.global.response.CommonResponse;
import com.withus.withus.global.response.ResponseCode;
import com.withus.withus.member.dto.EmailRequestDto;
import com.withus.withus.member.dto.MemberResponseDto;
import com.withus.withus.member.dto.SignupRequestDto;
import com.withus.withus.member.service.MemberServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
      @PathVariable(name = "memberId") Long memberId
  ){
    MemberResponseDto memberResponseDto = memberService.getMember(memberId);
    return ResponseEntity.status(ResponseCode.GET_PROFILE.getHttpStatus())
        .body(CommonResponse.of(ResponseCode.GET_PROFILE, memberResponseDto));
  }

}