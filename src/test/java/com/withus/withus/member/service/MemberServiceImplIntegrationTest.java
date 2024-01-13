package com.withus.withus.member.service;

import static org.junit.jupiter.api.Assertions.*;

import com.withus.withus.club.service.ClubMemberServiceImpl;
import com.withus.withus.club.service.ClubServiceImpl;
import com.withus.withus.global.config.EmailConfig;
import com.withus.withus.global.exception.BisException;
import com.withus.withus.global.s3.S3Util;
import com.withus.withus.global.security.jwt.RefreshTokenRepository;
import com.withus.withus.global.utils.EmailService;
import com.withus.withus.global.utils.RedisService;
import com.withus.withus.member.dto.EmailRequestDto;
import com.withus.withus.member.dto.SignupRequestDto;
import com.withus.withus.member.entity.Member;
import com.withus.withus.member.repository.MemberRepository;
import com.withus.withus.member.repository.ReportMemberRepository;
import java.time.Duration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class MemberServiceImplIntegrationTest {

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  ReportMemberRepository reportMemberRepository;

  @Autowired
  RefreshTokenRepository refreshTokenRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  MemberServiceImpl memberService;

  @Autowired
  RedisService redisService;

  @Autowired
  EmailService emailService;

  @Autowired
  EmailConfig emailConfig;

  @Autowired
  ClubMemberServiceImpl clubMemberService;

  @Autowired
  ClubServiceImpl clubService;

  @Autowired
  S3Util s3Util;

  String loginname;
  String password;
  String email;
  String username;
  String authCode;
  SignupRequestDto signupRequestDto;

  @BeforeEach
  void setup() {
    memberService = new MemberServiceImpl(memberRepository, reportMemberRepository, passwordEncoder,
       emailService, redisService, emailConfig, clubMemberService, clubService, s3Util);
    loginname = "testLoginname";
    password = "testPassword";
    email = "testEmail@naver.com";
    username = "testUsername";
    authCode = "123456";

    signupRequestDto = new SignupRequestDto(loginname, password, username, email, authCode);
  }

  @AfterEach
  void after() {
    memberRepository.deleteAll();
  }

  @Nested
  @DisplayName("인증코드 Email 발송 테스트")
  class sendEmailTest {

    @Test
    @DisplayName("Email 발송 성공")
    void sendEmailTest_success() {
      // given
      EmailRequestDto emailRequestDto = new EmailRequestDto(email);

      // when
      memberService.sendAuthCodeToEmail(emailRequestDto);
      String redisCode = redisService.getValues("AuthCode " + email);

      // then
      assertNotNull(redisCode);
    }

    @Test
    @DisplayName("Email 발송 실패 - Email 중복")
    void sendEmailTest_duplicationEmail() {
      // given
      EmailRequestDto emailRequestDto = new EmailRequestDto(email);
      memberRepository.save(Member.createMember(loginname, password, email, username));

      // when - then
      assertThrows(BisException.class,
          () -> memberService.sendAuthCodeToEmail(emailRequestDto));
    }

  }

  @Nested
  @DisplayName("회원가입 테스트")
  class signupTest {

    @Test
    @DisplayName("회원가입 성공")
    void signupTest_success () {
      // given
      redisService.setValues("AuthCode " + email, authCode, Duration.ofMillis(300000));

      // when
      memberService.signup(signupRequestDto);

      // then
      assertNotNull(memberRepository);
      assertEquals(email, memberRepository.findMemberByLoginnameAndIsActive(loginname,true).get().getEmail());
    }

    @Test
    @DisplayName("회원가입 실패 - authCode 불일치")
    void signupTest_NotMatchedAuthCode () {
      // given
      SignupRequestDto signupRequestDto2 = new SignupRequestDto(loginname, password, email, username, "av234c51");

      // when - then
      assertThrows(BisException.class,
          () -> memberService.signup(signupRequestDto2));
    }

    @Test
    @DisplayName("회원가입 실패 - username 중복")
    void signupTest_duplicationUsername () {
      // given
      memberRepository.save(Member.createMember(loginname, password, email, username));
      redisService.setValues("AuthCode " + email, authCode, Duration.ofMillis(300000));

      // when - then
      assertThrows(BisException.class,
          () -> memberService.signup(signupRequestDto));
    }
  }
}