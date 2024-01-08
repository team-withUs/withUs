package com.withus.withus.member.service;

import com.withus.withus.global.config.EmailConfig;
import com.withus.withus.global.exception.BisException;
import com.withus.withus.global.exception.ErrorCode;
import com.withus.withus.global.security.jwt.RefreshTokenRepository;
import com.withus.withus.global.utils.EmailService;
import com.withus.withus.global.utils.RedisService;
import com.withus.withus.member.dto.EmailRequestDto;
import com.withus.withus.member.repository.MemberRepository;
import java.time.Duration;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService{

  private static final String AUTH_CODE_PREFIX = "AuthCode ";

  private final MemberRepository memberRepository;

  private final RefreshTokenRepository refreshTokenRepository;

  private final EmailService emailService;

  private final RedisService redisService;

  private final EmailConfig emailConfig;

  @Override
  public void sendAuthCodeToEmail(EmailRequestDto emailRequestDto) {
    String email = emailRequestDto.email();

    sameMemberInDBByEmail(email);

    String title = "회원가입 이메일 인증 번호";
    String authCode = emailService.createCode();
    emailService.sendEmail(email, title, authCode);

    // 이메일 인증 요청 시 인증 번호 Redis에 저장 ( key = "AuthCode " + Email / value = AuthCode )
    redisService.setValues(AUTH_CODE_PREFIX + email,
        authCode, Duration.ofMillis(emailConfig.authCodeExpirationMillis));
  }

  public void sameMemberInDBByEmail(String email) {
    if (memberRepository.existsUserByEmail(email)) {
      throw new BisException(ErrorCode.DUPLICATE_EMAIL);
    }
  }
}
