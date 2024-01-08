package com.withus.withus.member.service;

import com.withus.withus.global.config.EmailConfig;
import com.withus.withus.global.exception.BisException;
import com.withus.withus.global.exception.ErrorCode;
import com.withus.withus.global.security.jwt.RefreshTokenRepository;
import com.withus.withus.global.utils.EmailService;
import com.withus.withus.global.utils.RedisService;
import com.withus.withus.member.dto.EmailRequestDto;
import com.withus.withus.member.dto.SignupRequestDto;
import com.withus.withus.member.entity.Member;
import com.withus.withus.member.repository.MemberRepository;
import java.time.Duration;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService{

  private static final String AUTH_CODE_PREFIX = "AuthCode ";

  private final MemberRepository memberRepository;

  private final PasswordEncoder passwordEncoder;

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

  @Override
  public void signup(SignupRequestDto signupRequestDto) {
    String loginname = signupRequestDto.loginname();
    String username = signupRequestDto.username();
    String email = signupRequestDto.email();
    String authCode = signupRequestDto.code();

    // 이메일 인증검사
    emailVerification(email, authCode);

    // 아이디 중복검사
    sameMemberInDBByLoginname(loginname);

    // 이름 중복검사
    sameMemberInDBByUsername(username);

    // 비밀번호 암호화
    String password = passwordEncoder.encode(signupRequestDto.password());

    // 새 멤버 등록
    Member member = new Member(loginname, password, email, username);
    memberRepository.save(member);

  }

  public void sameMemberInDBByLoginname(String loginname) {
    if (memberRepository.existsUserByLoginname(loginname)) {
      throw new BisException(ErrorCode.DUPLICATE_MEMBER);
    }
  }

  public void sameMemberInDBByUsername(String username) {
    if (memberRepository.existsUserByUsername(username)) {
      throw new BisException(ErrorCode.DUPLICATE_USERNAME);
    }
  }

  public Member findUserInDBById(Long id) {
    return memberRepository.findById(id).orElseThrow(() ->
        new BisException(ErrorCode.NOT_FOUND_MEMBER)
    );
  }

  public void sameMemberInDBByEmail(String email) {
    if (memberRepository.existsUserByEmail(email)) {
      throw new BisException(ErrorCode.DUPLICATE_EMAIL);
    }
  }

  public void emailVerification(String email, String authCode) {
    //관리자용 테스트 인증번호 추후에 테스트완료 후 삭제 예정
    if (authCode.equals("777777")) {
      return;
    }

    String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + email);
    if (!(redisService.checkExistsValue(redisAuthCode) && redisAuthCode.equals(authCode))) {
      throw new BisException(ErrorCode.NOT_MATCH_AUTHCODE);
    } else {
      redisService.deleteValues(redisAuthCode);
    }
  }
}
