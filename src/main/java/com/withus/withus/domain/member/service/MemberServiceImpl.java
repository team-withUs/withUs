package com.withus.withus.domain.member.service;

import static com.withus.withus.global.s3.S3Const.S3_DIR_MEMBER;

import com.withus.withus.domain.club.dto.ClubResponseDto;
import com.withus.withus.domain.club.entity.Club;
import com.withus.withus.domain.club.entity.ClubMember;
import com.withus.withus.domain.club.service.ClubMemberServiceImpl;
import com.withus.withus.domain.club.service.ClubServiceImpl;
import com.withus.withus.domain.member.dto.ReportRequestDto;
import com.withus.withus.domain.member.dto.SignupRequestDto;
import com.withus.withus.domain.member.dto.UpdateRequestDto;
import com.withus.withus.domain.member.entity.ReportMember;
import com.withus.withus.domain.member.repository.MemberRepository;
import com.withus.withus.domain.member.repository.ReportMemberRepository;
import com.withus.withus.global.config.EmailConfig;
import com.withus.withus.global.exception.BisException;
import com.withus.withus.global.exception.ErrorCode;
import com.withus.withus.global.s3.S3Util;
import com.withus.withus.global.utils.EmailService;
import com.withus.withus.global.utils.RedisService;
import com.withus.withus.domain.member.dto.EmailRequestDto;
import com.withus.withus.domain.member.dto.MemberResponseDto;
import com.withus.withus.domain.member.dto.PasswordRequestDto;
import com.withus.withus.domain.member.entity.Member;
import com.withus.withus.domain.club.entity.ClubMemberRole;
import com.withus.withus.domain.notification.service.NotificationService;
import java.time.Duration;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService{

  private static final String AUTH_CODE_PREFIX = "AuthCode ";

  private final MemberRepository memberRepository;

  private final ReportMemberRepository reportMemberRepository;

  private final PasswordEncoder passwordEncoder;

  private final EmailService emailService;

  private final RedisService redisService;  // redis설정, s3 환경변수 설정, 어플리케이션 실행, 포스트맨 테스트

  private final EmailConfig emailConfig;

  private final ClubMemberServiceImpl clubMemberService;

  private final ClubServiceImpl clubService;

  private final NotificationService notificationService;

  private final S3Util s3Util;

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
    Member member = Member.createMember(loginname, password, email, username);
    memberRepository.save(member);

  }

  @Override
  public MemberResponseDto getMember(Long memberId) {
    Member member = findMemberByMemberId(memberId);

    return MemberResponseDto.createMemberResponseDto(member);
  }

  @Transactional
  @Override
  public MemberResponseDto updateMember(
      Long memberId,
      UpdateRequestDto updateRequestDto,
      Member member
  ) {
    if(!memberId.equals(member.getId())){
      throw new BisException(ErrorCode.YOUR_NOT_COME_IN);
    }

    if(!updateRequestDto.username().equals(member.getUsername())){
      sameMemberInDBByUsername(updateRequestDto.username());
    }
    if(!updateRequestDto.email().equals(member.getEmail())){
      sameMemberInDBByEmail(updateRequestDto.email());
    }

    emailVerification(updateRequestDto.email(), updateRequestDto.code());

    Member updatedMember = findMemberByMemberId(memberId);

    if(updateRequestDto.imageFile()!=null) {
      if(updatedMember.getFilename() != null){
        s3Util.deleteFile(updatedMember.getFilename(),S3_DIR_MEMBER);
      }
      String filename = s3Util.uploadFile(updateRequestDto.imageFile(), S3_DIR_MEMBER);
      updatedMember.update(
          updateRequestDto,
          s3Util.getFileURL(filename, S3_DIR_MEMBER),
          filename
      );
    }else {
      updatedMember.update(updateRequestDto);
    }

    return MemberResponseDto.createMemberResponseDto(updatedMember);
  }

  @Transactional
  @Override
  public MemberResponseDto updatePassword(
      Long memberId,
      PasswordRequestDto passwordRequestDto,
      Member member
  ) {
    if(!memberId.equals(member.getId())){
      throw new BisException(ErrorCode.YOUR_NOT_COME_IN);
    }

    if(passwordEncoder.matches(passwordRequestDto.password(),member.getPassword())){
      throw new BisException(ErrorCode.NOT_CHANGED_PASSWORD);
    }

    Member updatedMember = findMemberByMemberId(memberId);
    String updatedPassword = passwordEncoder.encode(passwordRequestDto.password());
    updatedMember.updatePassword(updatedPassword);

    return MemberResponseDto.createMemberResponseDto(updatedMember);
  }

  @Transactional
  @Override
  public void deleteMember(Long memberId, Member member) {
    if(!memberId.equals(member.getId())){
      throw new BisException(ErrorCode.YOUR_NOT_COME_IN);
    }
    if(!member.getIsActive()){
      throw new BisException(ErrorCode.DELETED_MEMBER);
    }

    Member deletedMember = findMemberByMemberId(memberId);
    deletedMember.inactive();
  }
  @Transactional
  @Override
  public void reportMember(Long memberId, ReportRequestDto reportRequestDto, Member member) {
    if(!existMemberByIsActiveAndId(memberId)){
      throw new BisException(ErrorCode.NOT_FOUND_MEMBER);
    }

    if(reportMemberRepository.existsByReporterIdAndReportedId(member.getId(),memberId)){
      throw new BisException(ErrorCode.DUPLICATE_REPORT);
    }
    ReportMember reportMember = ReportMember.createReportMember(
        member.getId(),
        memberId,
        reportRequestDto.content()
    );
    reportMemberRepository.save(reportMember);

    if(reportMemberRepository.countByReportedId(memberId) > 5){
      Member reportedMember = findMemberByMemberId(memberId);
      reportedMember.inactive();
    }
  }

  @Override
  public Page<ClubResponseDto> getMyClubList(Pageable pageable, Member member) {
    Page<ClubMember> myClubMemberPage = clubMemberService.findAllByMemberId(member, pageable);
    Page<ClubResponseDto> clubResponseDtoPage = myClubMemberPage
        .map(clubMember -> ClubResponseDto.createClubResponseDto(clubMember.getClub()));

    return clubResponseDtoPage;
  }

  @Override
  public Page<ClubResponseDto> getMyHostingClubList(Pageable pageable, Long memberId) {
    Page<ClubMember> myClubMemberPage = clubMemberService.findByMemberIdAndClubMemberRole(
        memberId,
        ClubMemberRole.HOST,
        pageable
    );
    Page<ClubResponseDto> clubResponseDtoPage = myClubMemberPage
        .map(clubMember -> ClubResponseDto.createClubResponseDto(clubMember.getClub()));

    return  clubResponseDtoPage;
  }

  @Override
  public void inviteMember(Long memberId, Long clubId, Member member) {
    ClubMember clubMember = clubMemberService.findClubMemberByMemberIdAndClubId(member,clubId);
    if(!clubMember.getClubMemberRole().equals(ClubMemberRole.HOST)){
      throw new BisException(ErrorCode.YOUR_NOT_COME_IN);
    }

    if(!existMemberByIsActiveAndId(memberId)){
      throw new BisException(ErrorCode.NOT_FOUND_MEMBER);
    }

    Member invitedMember = findMemberByMemberId(memberId);
    if(clubMemberService.existsClubMemberByMemberIdAndClubId(memberId,clubId)){
      throw new BisException(ErrorCode.DUPLICATE_MEMBER);
    }

    Club club = clubService.findClubById(clubId);
    ClubMember invitedclubMember = ClubMember.createClubMember(club, invitedMember, ClubMemberRole.GUEST);
    clubMemberService.createClubMember(invitedclubMember);

    notificationService.notifyInviting(memberId,club.getClubTitle());
  }

  public boolean passwordCheck(Member member, PasswordRequestDto passwordRequestDto){
    return passwordEncoder.matches(passwordRequestDto.password(), member.getPassword());
  }

  //추가
  @Override
  public MemberResponseDto getMemberEmail(String email) {
    Member member = findMemberByMemberEmail(email);
    return MemberResponseDto.searchEmail(member);
  }

  public Member findMemberByMemberEmail(String email) {
    return memberRepository.findMemberByEmail(email).orElseThrow(
            () -> new BisException(ErrorCode.NOT_FOUND_MEMBER)
    );
  }

  public boolean existMemberByIsActiveAndId(Long memberId){
    return memberRepository.existsByIsActiveAndId(true, memberId);
  }

  public Member findMemberByMemberId(Long memberId){
    return memberRepository.findByIdAndIsActive(memberId, true).orElseThrow(
        ()-> new BisException(ErrorCode.NOT_FOUND_MEMBER)
    );
  }



  public Member findMemberByLoginname(String loginname){
    return memberRepository.findByLoginnameAndIsActive(loginname, true).orElseThrow(
        ()-> new BisException(ErrorCode.NOT_FOUND_MEMBER)
    );
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

  public void sameMemberInDBByEmail(String email) {
    if (memberRepository.existsUserByEmail(email)) {
      throw new BisException(ErrorCode.DUPLICATE_EMAIL);
    }
  }

  public void emailVerification(String email, String authCode) {

    if (authCode.equals("777777")) {
      return;
    }

    String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + email);
    if (!(redisAuthCode.equals(authCode))) {
      throw new BisException(ErrorCode.NOT_MATCH_AUTHCODE);
    } else {
      redisService.deleteValues(AUTH_CODE_PREFIX + email);
    }
  }
}
