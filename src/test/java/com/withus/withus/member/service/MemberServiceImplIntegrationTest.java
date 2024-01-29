package com.withus.withus.member.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.withus.withus.domain.club.entity.ClubCategory;
import com.withus.withus.domain.club.dto.ClubRequestDto;
import com.withus.withus.domain.club.dto.ClubResponseDto;
import com.withus.withus.domain.club.repository.ClubMemberRepository;
import com.withus.withus.domain.club.repository.ClubRepository;
import com.withus.withus.domain.club.service.ClubMemberServiceImpl;
import com.withus.withus.domain.club.service.ClubServiceImpl;
import com.withus.withus.domain.member.service.MemberServiceImpl;
import com.withus.withus.global.config.EmailConfig;
import com.withus.withus.global.response.exception.BisException;
import com.withus.withus.global.response.exception.ErrorCode;
import com.withus.withus.global.utils.s3.S3Util;
import com.withus.withus.global.utils.EmailService;
import com.withus.withus.global.utils.RedisService;
import com.withus.withus.domain.member.dto.EmailRequestDto;
import com.withus.withus.domain.member.dto.MemberResponseDto;
import com.withus.withus.domain.member.dto.ReportRequestDto;
import com.withus.withus.domain.member.dto.SignupRequestDto;
import com.withus.withus.domain.member.dto.UpdateRequestDto;
import com.withus.withus.domain.member.entity.Member;
import com.withus.withus.domain.member.entity.ReportMember;
import com.withus.withus.domain.member.repository.MemberRepository;
import com.withus.withus.domain.member.repository.ReportMemberRepository;
import com.withus.withus.domain.notification.service.NotificationService;
import java.time.Duration;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
class MemberServiceImplIntegrationTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ReportMemberRepository reportMemberRepository;

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
    ClubRepository clubRepository;

    @Autowired
    ClubMemberRepository clubMemberRepository;

    @Autowired
    NotificationService notificationService;

    @Autowired
    S3Util s3Util;

    String loginname;
    String password;
    String email;
    String username;
    String authCode;
    SignupRequestDto signupRequestDto;
    Member member;

    @BeforeEach
    void setup() {
        memberService = new MemberServiceImpl(memberRepository, reportMemberRepository, passwordEncoder,
                emailService, redisService, emailConfig, clubMemberService, clubService, notificationService, s3Util);
        loginname = "testLoginname";
        password = "testPassword";
        email = "testEmail@naver.com";
        username = "testUsername";
        authCode = "123456";

        signupRequestDto = new SignupRequestDto(loginname, password, username, email, authCode);
    }

    @AfterEach
    void after() {
        clubMemberRepository.deleteAll();
        clubRepository.deleteAll();
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
        void signupTest_success() {
            // given
            redisService.setValues("AuthCode " + email, authCode, Duration.ofMillis(300000));

            // when
            memberService.signup(signupRequestDto);

            // then
            assertNotNull(memberRepository);
            assertEquals(email, memberRepository.findByLoginnameAndIsActive(loginname, true).get().getEmail());
        }

        @Test
        @DisplayName("회원가입 실패 - authCode 불일치")
        void signupTest_NotMatchedAuthCode() {
            // given
            SignupRequestDto signupRequestDto2 = new SignupRequestDto(loginname, password, email, username, "av234c51");

            // when - then
            assertThrows(BisException.class,
                    () -> memberService.signup(signupRequestDto2));
        }

        @Test
        @DisplayName("회원가입 실패 - username 중복")
        void signupTest_duplicationUsername() {
            // given
            memberRepository.save(Member.createMember(loginname, password, email, username));
            redisService.setValues("AuthCode " + email, authCode, Duration.ofMillis(300000));

            // when - then
            assertThrows(BisException.class,
                    () -> memberService.signup(signupRequestDto));
        }
    }

    @Nested
    @DisplayName("회원 불러오기 테스트")
    class getMemberTest {
        @BeforeEach
        void setMember() {
            member = Member.createMember(
                    loginname,
                    password,
                    email,
                    username
            );
        }

        @Test
        @DisplayName("회원 불러오기 성공")
        void getMember_success() {
            //given
            Member savedMember = memberRepository.save(member);

            //when
            MemberResponseDto memberResponseDto = memberService.getMember(member.getId());

            //then
            Assertions.assertEquals(memberResponseDto.username(), savedMember.getUsername());
        }

        @Test
        @DisplayName("회원 불러오기 실패 - 존재하지 않는 회원")
        void getMember_notFoundMember() {
            //given
            Member savedMember = memberRepository.save(member);

            //when
            BisException e = Assertions.assertThrows(BisException.class,
                    () ->
                            memberService.getMember(2L)
            );
            memberService.getMember(savedMember.getId());

            //then
            Assertions.assertEquals(e.getErrorCode(), ErrorCode.NOT_FOUND_MEMBER);
            Assertions.assertDoesNotThrow(
                    () -> memberService.getMember(savedMember.getId())
            );
        }
    }

    @Nested
    @DisplayName("회원 정보 수정 테스트")
    class updateMemberTest {

        MockMultipartFile file;

        @BeforeEach
        void setMember() {
            member = Member.createMember(
                    loginname,
                    password,
                    email,
                    username
            );
            file = new MockMultipartFile(
                    "테스트 이미지.png",
                    "테스트 이미지".getBytes()
            );
        }

        @Test
        @DisplayName("회원 프로필 수정 성공")
        @Transactional
        void updateMember_success() {
            //given
            Member savedMember = memberRepository.save(member);
            UpdateRequestDto updateRequestDto = new UpdateRequestDto(
                    "username33",
                    "user33@email.com",
                    "[\"asdf \",\"asdf \",\"adsa \",\"1234 \"]",
                    "소개",
                    file
            );
            Member updatedMember = memberRepository.findById(savedMember.getId()).get();

            //when
            memberService.updateMember(
                    savedMember.getId(),
                    updateRequestDto, savedMember
            );

            //then
            Assertions.assertEquals(updatedMember.getUsername(), updateRequestDto.username());
            Assertions.assertEquals(updatedMember.getEmail(), updateRequestDto.email());
        }

        @Test
        @DisplayName("회원 프로필 수정 실패 - 비밀번호 확인 불일치")
        @Transactional
        void updateMember_notMatchPasswordCheck() {
            //given
            Member savedMember = memberRepository.save(member);

            //when
            BisException e = Assertions.assertThrows(BisException.class, () -> {
                UpdateRequestDto updateRequestDto = new UpdateRequestDto(
                        "username33",
                        "user33@email.com",
                        "[\"asdf \",\"asdf \",\"adsa \",\"1234 \"]",
                        "자기소개",
                        file
                );
                memberService.updateMember(
                        savedMember.getId(),
                        updateRequestDto, savedMember
                );
            });

            //then
            Assertions.assertEquals(e.getErrorCode(), ErrorCode.NOT_MATCH_PASSWORD_CHECK);
        }

        @Test
        @DisplayName("회원 프로필 수정 실패 - 권한 없음")
        @Transactional
        void updateMember_yourNotComeIn() {
            //given
            Member savedMember = memberRepository.save(member);
            Member member2 = Member.createMember(
                    "user22",
                    "123456789",
                    "user22@email.com",
                    "username22"
            );
            Member savedMember2 = memberRepository.save(member2);

            UpdateRequestDto updateRequestDto = new UpdateRequestDto(
                    "username33",
                    "user33@email.com",
                    "[\"asdf \",\"asdf \",\"adsa \",\"1234 \"]",
                    "소개",
                    file
            );

            //when
            BisException e = Assertions.assertThrows(BisException.class,
                    () -> memberService.updateMember(
                            savedMember.getId(),
                            updateRequestDto,
                            savedMember2
                    )
            );

            //then
            assertEquals(e.getErrorCode(), ErrorCode.YOUR_NOT_COME_IN);
        }
    }

    @Nested
    @DisplayName("회원 탈퇴 테스트")
    class deleteMember {
        @BeforeEach
        void setMember() {
            member = Member.createMember(
                    loginname,
                    password,
                    email,
                    username
            );
        }

        @Test
        @DisplayName("회원 탈퇴 성공")
        @Transactional
        void deleteMember_success() {
            //given
            memberRepository.save(member);

            //when
            memberService.deleteMember(member.getId(), member);

            //then
            Assertions.assertEquals(member.getIsActive(), false);
        }

        @Test
        @DisplayName("회원 탈퇴 실패 - 권한 없음")
        @Transactional
        void deleteMember_yourNotComeIn() {
            //given
            memberRepository.save(member);
            Member member2 = Member.createMember(
                    "user22",
                    "123456789",
                    "user22@email.com",
                    "username22"
            );
            Member savedMember2 = memberRepository.save(member2);

            //when
            BisException e = Assertions.assertThrows(BisException.class,
                    () -> memberService.deleteMember(
                            member.getId(),
                            savedMember2
                    )
            );

            //then
            Assertions.assertEquals(e.getErrorCode(), ErrorCode.YOUR_NOT_COME_IN);
        }

        @Test
        @DisplayName("회원 탈퇴 실패 - 이미 탈퇴한 회원")
        @Transactional
        void deleteMember_deletedMember() {
            //given
            memberRepository.save(member);

            //when
            memberService.deleteMember(member.getId(), member);
            BisException e = Assertions.assertThrows(BisException.class,
                    () -> memberService.deleteMember(member.getId(), member)
            );

            //then
            Assertions.assertEquals(e.getErrorCode(), ErrorCode.DELETED_MEMBER);
        }
    }

    @Nested
    @DisplayName("회원 신고 테스트")
    class reportMember {

        Member member2;

        @BeforeEach
        void setMember() {
            member = Member.createMember(
                    loginname,
                    password,
                    email,
                    username
            );

            member2 = Member.createMember(
                    "user22",
                    "123456789",
                    "user22@email.com",
                    "username22"
            );
        }

        @Test
        @DisplayName("회원 신고 성공")
        @Transactional
        void reportMember_success() {
            //given
            Member reportedMember = memberRepository.save(member);
            Member reporterMember = memberRepository.save(member2);
            ReportRequestDto reportRequestDto = new ReportRequestDto("신고 내용");

            //when
            memberService.reportMember(reportedMember.getId(), reportRequestDto, reporterMember);
            ReportMember reportMember = reportMemberRepository.findByReportedIdAndReporterId(
                    reportedMember.getId(),
                    reporterMember.getId()
            );

            //then
            Assertions.assertTrue(reportMemberRepository.existsByReporterIdAndReportedId(
                    member2.getId(),
                    member.getId()
            ));
            Assertions.assertEquals(reportMember.getContent(), reportRequestDto.content());
        }

        @Test
        @DisplayName("회원 신고 실패 - 존재하지 않는 회원")
        void reportMember_notFoundMember() {
            //given
            Member reporterMember = memberRepository.save(member);
            ReportRequestDto reportRequestDto = new ReportRequestDto("신고 내용");

            //when
            BisException e = Assertions.assertThrows(BisException.class,
                    () -> memberService.reportMember(2L, reportRequestDto, reporterMember)
            );

            //then
            Assertions.assertEquals(e.getErrorCode(), ErrorCode.NOT_FOUND_MEMBER);
            Assertions.assertFalse(reportMemberRepository.existsByReporterIdAndReportedId(
                    reporterMember.getId(),
                    2L)
            );
        }

        @Test
        @DisplayName("회원 신고 실패 - 이미 신고한 회원")
        void reportMember_duplicateReport() {
            //given
            Member reportedMember = memberRepository.save(member);
            Member reporterMember = memberRepository.save(member2);
            ReportRequestDto reportRequestDto = new ReportRequestDto("신고 내용");

            //when
            memberService.reportMember(reportedMember.getId(), reportRequestDto, reporterMember);
            BisException e = Assertions.assertThrows(BisException.class,
                    () -> memberService.reportMember(reportedMember.getId(), reportRequestDto, reporterMember)
            );

            //then
            Assertions.assertEquals(e.getErrorCode(), ErrorCode.DUPLICATE_REPORT);
        }
    }

    @Nested
    @DisplayName("클럽 초대하기")
    class inviteMember {

        Member member2;
        ClubRequestDto clubRequestDto;

        @BeforeEach
        void setMember() {
            member = Member.createMember(
                    loginname,
                    password,
                    email,
                    username
            );

            member2 = Member.createMember(
                    "user22",
                    "123456789",
                    "user22@email.com",
                    "username22"
            );

            MockMultipartFile file = new MockMultipartFile(
                    "테스트 이미지.png",
                    "테스트 이미지".getBytes()
            );

            clubRequestDto = new ClubRequestDto(
                    "클럽 이름",
                    "클럽 소개",
                    ClubCategory.SPORTS,
                    file,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
        }

        @Test
        @DisplayName("클럽 초대하기 성공")
        @Transactional
        void inviteMember_success() {
            //given
            Member guest = memberRepository.save(member);
            Member host = memberRepository.save(member2);
            Long clubId = clubService.createClub(clubRequestDto, host, clubRequestDto.imageFile()).clubId();

            //when
            memberService.inviteMember(guest.getId(), clubId, host);

            //then
            Assertions.assertTrue(clubMemberService.existsClubMemberByMemberIdAndClubId(
                            guest.getId(),
                            clubId
                    )
            );
            Assertions.assertEquals(clubMemberService.findClubMemberByMemberIdAndClubId(guest, clubId).getMember(), guest);
        }

        @Test
        @DisplayName("클럽 초대하기 실패 - 권한 없음")
        void inviteMember_yourNotComeIn() {
            //given
            Member guest = memberRepository.save(member);
            Member member3 = Member.createMember(
                    "user33",
                    "123456789",
                    "user33@email.com",
                    "username33"
            );
            Member guest2 = memberRepository.save(member3);
            Member host = memberRepository.save(member2);
            Long clubId = clubService.createClub(clubRequestDto, host, clubRequestDto.imageFile()).clubId();

            //when
            BisException e = Assertions.assertThrows(BisException.class,
                    () -> memberService.inviteMember(guest.getId(), clubId, guest2)
            );

            //then
            Assertions.assertEquals(e.getErrorCode(), ErrorCode.YOUR_NOT_COME_IN);
            Assertions.assertDoesNotThrow(() -> memberService.inviteMember(guest.getId(), clubId, host));
        }

        @Test
        @DisplayName("클럽 초대하기 실패 - 존재하지 않는 회원")
        void inviteMember_notFoundMember() {
            //given
            Member guest = memberRepository.save(member);
            Member host = memberRepository.save(member2);
            Long clubId = clubService.createClub(clubRequestDto, host, clubRequestDto.imageFile()).clubId();

            //when
            BisException e = Assertions.assertThrows(BisException.class,
                    () -> memberService.inviteMember(3L, clubId, host)
            );

            //then
            Assertions.assertEquals(e.getErrorCode(), ErrorCode.NOT_FOUND_MEMBER);
            Assertions.assertDoesNotThrow(() -> memberService.inviteMember(guest.getId(), clubId, host));
        }

        @Test
        @DisplayName("클럽 초대하기 실패 - 이미 초대된 멤버")
        void inviteMember_duplicateMember() {
            //given
            Member guest = memberRepository.save(member);
            Member host = memberRepository.save(member2);
            Long clubId = clubService.createClub(clubRequestDto, host, clubRequestDto.imageFile()).clubId();

            //when
            memberService.inviteMember(guest.getId(), clubId, host);
            BisException e = Assertions.assertThrows(BisException.class,
                    () -> memberService.inviteMember(guest.getId(), clubId, host)
            );

            //then
            Assertions.assertEquals(e.getErrorCode(), ErrorCode.DUPLICATE_MEMBER);
        }
    }

    @Nested
    @DisplayName("가입한 클럽 목록 불러오기 테스트")
    class getMyClubListTest {

        ClubRequestDto clubRequestDto;
        ClubRequestDto clubRequestDto2;
        Pageable pageable;

        @BeforeEach
        void setMember() {
            member = Member.createMember(
                    loginname,
                    password,
                    email,
                    username
            );
            MockMultipartFile file = new MockMultipartFile(
                    "테스트 이미지.png",
                    "테스트 이미지".getBytes()
            );

            clubRequestDto = new ClubRequestDto(
                    "클럽 이름",
                    "클럽 소개",
                    ClubCategory.SPORTS,
                    file,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );

            clubRequestDto2 = new ClubRequestDto(
                    "클럽 이름2",
                    "클럽 소개2",
                    ClubCategory.SPORTS,
                    file,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
        }

        @Test
        @DisplayName("가입한 클럽 목록 불러오기 성공")
        @Transactional
        void getMyClubList_success() {
            //given
            Member host = memberRepository.save(member);
            clubService.createClub(clubRequestDto, host, clubRequestDto.imageFile());
            clubService.createClub(clubRequestDto2, host, clubRequestDto.imageFile());

            //when
            Page<ClubResponseDto> clubResponseDtoPage = memberService.getMyClubList(pageable, host);

            //then
            Assertions.assertEquals(clubResponseDtoPage.getContent().size(), 2);
            Assertions.assertEquals(clubResponseDtoPage.getContent().get(0).clubTitle(), clubRequestDto.clubTitle());
            Assertions.assertEquals(clubResponseDtoPage.getContent().get(1).clubTitle(), clubRequestDto2.clubTitle());
        }
    }
}


