package com.withus.withus.member.service;

import com.withus.withus.member.dto.EmailRequestDto;
import com.withus.withus.member.dto.MemberResponseDto;
import com.withus.withus.member.dto.SignupRequestDto;

public interface MemberService {

  void sendAuthCodeToEmail(EmailRequestDto emailRequestDto);

  void signup(SignupRequestDto signupRequestDto);

  MemberResponseDto getMember(Long memberId);
}
