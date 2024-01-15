package com.withus.withus.member.service;

import static com.withus.withus.global.s3.S3Const.S3_DIR_MEMBER;

import com.withus.withus.club.dto.ClubResponseDto;
import com.withus.withus.club.entity.Club;
import com.withus.withus.club.entity.ClubMember;
import com.withus.withus.club.service.ClubMemberService;
import com.withus.withus.club.service.ClubMemberServiceImpl;
import com.withus.withus.club.service.ClubService;
import com.withus.withus.club.service.ClubServiceImpl;
import com.withus.withus.global.config.EmailConfig;
import com.withus.withus.global.exception.BisException;
import com.withus.withus.global.exception.ErrorCode;
import com.withus.withus.global.s3.S3Util;
import com.withus.withus.global.security.jwt.RefreshTokenRepository;
import com.withus.withus.global.utils.EmailService;
import com.withus.withus.global.utils.RedisService;
import com.withus.withus.member.dto.EmailRequestDto;
import com.withus.withus.member.dto.MemberResponseDto;
import com.withus.withus.member.dto.ReportRequestDto;
import com.withus.withus.member.dto.SignupRequestDto;
import com.withus.withus.member.dto.UpdateRequestDto;
import com.withus.withus.member.entity.Member;
import com.withus.withus.club.entity.ClubMemberRole;
import com.withus.withus.member.entity.ReportMember;
import com.withus.withus.member.repository.MemberRepository;
import com.withus.withus.member.repository.ReportMemberRepository;
import java.time.Duration;
import java.util.List;
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

  private final RedisService redisService;

  private final EmailConfig emailConfig;

  private final ClubMemberServiceImpl clubMemberService;

  private final ClubServiceImpl clubService;

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

    if(passwordEncoder.matches(updateRequestDto.password(), member.getPassword())){
      throw new BisException(ErrorCode.NOT_CHANGED_PASSWORD);
    }
    sameMemberInDBByUsername(updateRequestDto.username());
    sameMemberInDBByEmail(updateRequestDto.email());

    Member updatedMember = findMemberByMemberId(memberId);
    if(updatedMember.getFilename() != null){
      s3Util.deleteFile(updatedMember.getFilename(),S3_DIR_MEMBER);
    }

    if(!updateRequestDto.imageFile().getName().isEmpty()) {
      String filename = s3Util.uploadFile(updateRequestDto.imageFile(), S3_DIR_MEMBER);
      updatedMember.update(
          updateRequestDto,
          passwordEncoder.encode(updateRequestDto.password()),
          s3Util.getFileURL(filename, S3_DIR_MEMBER),
          filename
      );
    }else {
      updatedMember.update(
          updateRequestDto,
          passwordEncoder.encode(updateRequestDto.password())
      );
    }

    return MemberResponseDto.createMemberResponseDto(
        updatedMember
    );
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
  public List<ClubResponseDto> getMyClubList(Pageable pageable, Member member) {
    Page<ClubMember> myClubMemberPage = clubMemberService.findAllByMemberId(member,pageable);
    List<ClubResponseDto> clubResponseDtoList = myClubMemberPage
        .map(clubMember -> ClubResponseDto.createClubResponseDto(clubMember.getClub()))
        .getContent();

    return clubResponseDtoList;
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
  }

  public boolean existMemberByIsActiveAndId(Long memberId){
    return memberRepository.existsByIsActiveAndId(true, memberId);
  }

  public Member findMemberByMemberId(Long memberId){
    return memberRepository.findMemberByIdAndIsActive(memberId, true).orElseThrow(
        ()-> new BisException(ErrorCode.NOT_FOUND_MEMBER)
    );
  }

  public Member findMemberByLoginname(String loginname){
    return memberRepository.findMemberByLoginnameAndIsActive(loginname, true).orElseThrow(
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
