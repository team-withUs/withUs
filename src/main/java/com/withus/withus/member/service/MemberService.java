package com.withus.withus.member.service;

import com.withus.withus.member.dto.EmailRequestDto;

public interface MemberService {

  void sendAuthCodeToEmail(EmailRequestDto emailRequestDto);
}
