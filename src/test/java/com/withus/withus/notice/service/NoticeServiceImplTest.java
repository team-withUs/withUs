package com.withus.withus.notice.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.withus.withus.category.entity.ClubCategory;
import com.withus.withus.club.entity.Club;
import com.withus.withus.club.entity.ClubMember;
import com.withus.withus.club.entity.ClubMemberRole;
import com.withus.withus.club.repository.ClubMemberRepository;
import com.withus.withus.club.repository.ClubRepository;
import com.withus.withus.global.exception.BisException;
import com.withus.withus.global.exception.ErrorCode;
import com.withus.withus.member.entity.Member;
import com.withus.withus.member.repository.MemberRepository;
import com.withus.withus.notice.dto.NoticeRequestDto;
import com.withus.withus.notice.dto.NoticeResponseDto;
import com.withus.withus.notice.dto.PageableDto;
import com.withus.withus.notice.dto.ReportRequestDto;
import com.withus.withus.notice.entity.Notice;
import com.withus.withus.notice.entity.NoticeCategory;
import com.withus.withus.notice.entity.ReportNotice;
import com.withus.withus.notice.repository.NoticeRepository;
import com.withus.withus.notice.repository.ReportRepository;
import com.withus.withus.notice.service.NoticeService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@ActiveProfiles("test")
@SpringBootTest
class NoticeServiceImplTest {


  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private ClubRepository clubRepository;

  @Autowired
  private NoticeRepository noticeRepository;

  @Autowired
  private NoticeService noticeService;

  @Autowired
  private ClubMemberRepository clubMemberRepository;

  @Autowired
  private ReportRepository reportRepository;



  Member testMember;
  Member testNotClubMember;
  Club testClub;
  Notice testNotice;


  @BeforeEach
  void setUp(){
    reportRepository.deleteAll();
    noticeRepository.deleteAll();
    clubRepository.deleteAll();
    clubMemberRepository.deleteAll();
    memberRepository.deleteAll();




    testMember = Member.createMember("testLogin", "1234", "aaa@gmail.com", "test1");
    memberRepository.save(testMember);

    testNotClubMember = Member.createMember("testNotClubMember", "1234", "bbb@gmail.com", "NotClubMember");
    memberRepository.save(testNotClubMember);

    testClub = Club.builder()
        .clubTitle("축구 클럽 모임")
        .content("축구클럽모임입니다.")
        .category(ClubCategory.SPORTS)
        .member(testMember)
        .filename("c5c2ad5e-38c5-4971-bb65-432efa8c24ea.png")
        .build();
    clubRepository.save(testClub);

    ClubMember clubMember = ClubMember.createClubMember(testClub, testMember, ClubMemberRole.HOST);
    clubMemberRepository.save(clubMember);

    NoticeRequestDto noticeRequestDto = new NoticeRequestDto("공지사항1", "공지사항입니다1.", "board", null);
    testNotice = Notice.createNotice(noticeRequestDto, testMember, testClub, NoticeCategory.BOARD);
    noticeRepository.save(testNotice);

  }

  @Nested
  @DisplayName("게시판 생성")
  class createNotice{

    @Test
    @Rollback(value = false)
    @DisplayName("게시판 생성 성공")
    void createNotice_Success() {

      //given
      NoticeRequestDto noticeRequestDto =new NoticeRequestDto("공지사항1", "공지사항입니다1.", "Notice", null);


      //when
      NoticeResponseDto responseDto = noticeService.createNotice(testClub.getId(), noticeRequestDto, testMember);


      //then
      assertEquals("공지사항1", responseDto.title());
      assertEquals("공지사항입니다1.", responseDto.content());

    }

    @Test
    @DisplayName("게시판 생성 실패(클럽멤버가 아닐경우)")
    void createNotice_Failure_ClubMember(){

      //given
      NoticeRequestDto requestDto =new NoticeRequestDto("공지사항1", "공지사항입니다1.", "Notice", null);


      //when
      BisException exception = assertThrows(BisException.class, ()-> {
        noticeService.createNotice(testClub.getId(), requestDto, testNotClubMember);
      });


      //then
      assertEquals(ErrorCode.NOT_FOUND_CLUB_MEMBER_EXIST, exception.getErrorCode());

    }

    @Test
    @DisplayName("게시판 생성 실패(클럽이 없을경우)")
    void createNotice_Failure_Club(){
      //given
      NoticeRequestDto requestDto =new NoticeRequestDto("공지사항1", "공지사항입니다1.", "Notice", null);


      //when
      BisException exception = assertThrows(BisException.class, ()-> {
        noticeService.createNotice(10L, requestDto, testMember);
      });


      //then
      assertEquals(ErrorCode.NOT_FOUND_CLUB, exception.getErrorCode());

    }

    @Test
    @DisplayName("게시판 생성 성공(이미지)")
    void createNotice_Success_Image(){
      //given
      MockMultipartFile file = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
      NoticeRequestDto noticeRequestDto =new NoticeRequestDto("공지사항1", "공지사항입니다1.", "Notice", file);


      //when
      NoticeResponseDto responseDto = noticeService.createNotice(testClub.getId(), noticeRequestDto, testMember);


      //then
      assertEquals("공지사항1", responseDto.title());
      assertEquals("공지사항입니다1.", responseDto.content());
      assertNotNull(responseDto.imageURL());

    }

  }

  @Nested
  @DisplayName("게시판 수정")
  class updateNotice{

    @Test
    @Rollback(value = false)
    @DisplayName("게시판 수정 성공")
    void updateNotice_Success(){

      //given
      NoticeRequestDto updateRequestDto = new NoticeRequestDto("공지사항1 수정", "공지사항입니다1. 수정", "board", null);


      //when
      NoticeResponseDto responseDto = noticeService.updateNotice(testClub.getId(), testNotice.getId(), updateRequestDto, testMember);


      //then
      assertEquals(updateRequestDto.title(), responseDto.title());
      assertEquals(updateRequestDto.content(), responseDto.content());

    }

    @Test
    @DisplayName("게시판 수정 실패(클럽멤버가 아닐경우)")
    void updateNotice_Failure_ClubMember(){

      //given
      NoticeRequestDto updateRequestDto = new NoticeRequestDto("공지사항1 수정", "공지사항입니다1. 수정", "board", null);


      //when
      BisException exception = assertThrows(BisException.class, ()-> {
        noticeService.updateNotice(testClub.getId(), testNotice.getId(), updateRequestDto, testNotClubMember);
      });


      //then
      assertEquals(ErrorCode.YOUR_NOT_COME_IN, exception.getErrorCode());

    }

    @Test
    @DisplayName("게시판 수정 실패(클럽이 없을경우)")
    void updateNotice_Failure_Club(){

      //given
      NoticeRequestDto updateRequestDto = new NoticeRequestDto("공지사항1 수정", "공지사항입니다1. 수정", "board", null);


      //when
      BisException exception = assertThrows(BisException.class, ()-> {
        noticeService.updateNotice(10L, testNotice.getId(), updateRequestDto, testMember);
      });


      //then
      assertEquals(ErrorCode.NOT_FOUND_CLUB, exception.getErrorCode());

    }

  }

  @Nested
  @DisplayName("게시판 삭제")
  class deleteNotice{

    @Test
    @Rollback(value = false)
    @DisplayName("게시판 삭제 성공")
    void deleteNotice_Success(){

      //given
      // when
      noticeService.deleteNotice(testClub.getId(), testNotice.getId(), testMember);


      //then
      assertEquals(false, testNotice.getIsActive());

    }

    @Test
    @DisplayName("게시판 삭제 실패(클럽멤버가 아닐경우)")
    void deleteNotice_Failure_ClubMember(){

      //given
      //when
      BisException exception = assertThrows(BisException.class, ()-> {
        noticeService.deleteNotice(testClub.getId(), testNotice.getId(), testNotClubMember);
      });


      //then
      assertEquals(ErrorCode.YOUR_NOT_COME_IN, exception.getErrorCode());

    }

    @Test
    @DisplayName("게시판 삭제 실패(클럽이 없을경우)")
    void deleteNotice_Failure_Club(){

      //given
      //when
      BisException exception = assertThrows(BisException.class, ()-> {
        noticeService.deleteNotice(10L, testNotice.getId(), testMember);
      });


      //then
      assertEquals(ErrorCode.NOT_FOUND_CLUB, exception.getErrorCode());

    }

  }

  @Nested
  @DisplayName("게시판 조회")
  class getNotice{

    @Test
    @Rollback(value = false)
    @DisplayName("게시판 조회 성공")
    void getNotice_Success(){

      //given
      //when
      NoticeResponseDto responseDto = noticeService.getNotice(testClub.getId(), testNotice.getId(), testMember);


      //then
      assertEquals(testNotice.getTitle(), responseDto.title());
      assertEquals(testNotice.getContent(), responseDto.content());

    }

    @Test
    @DisplayName("게시판 조회 실패(클럽이 없을경우)")
    void getNotice_Failure_Club(){
      //given
      //when
      BisException exception = assertThrows(BisException.class, ()-> {
        noticeService.getNotice(10L, testNotice.getId(), testMember);
      });

      //then
      assertEquals(ErrorCode.NOT_FOUND_CLUB, exception.getErrorCode());

    }


//    @Test
//    @Rollback(value = false)
//    @DisplayName("게시판 전체조회(페이징) 성공")
//    void getsNotice_Success(){
//
//      //given
//      NoticeRequestDto noticeRequestDto2 = new NoticeRequestDto("공지사항2", "공지사항입니다2.", "board", null);
//      NoticeRequestDto noticeRequestDto3 = new NoticeRequestDto("공지사항3", "공지사항입니다3.", "board", null);
//      Notice testNotice2 = Notice.createNotice(noticeRequestDto2, testMember, testClub, NoticeCategory.BOARD);
//      Notice testNotice3 = Notice.createNotice(noticeRequestDto3, testMember, testClub, NoticeCategory.BOARD);
//      noticeRepository.save(testNotice2);
//      noticeRepository.save(testNotice3);
//      PageableDto pageableDto = new PageableDto(1,3,"CreatedAt");
//
//
//      //when
//      List<NoticeResponseDto> list = noticeService.getsNotice(testClub.getId(), pageableDto);
//
//
//      //then
////      assertEquals("공지사항3", list.get(0).title());
//      assertEquals("공지사항2", list.get(1).title());
////      assertEquals("공지사항1", list.get(2).title());
//
//    }


    @Test
    @Rollback(value = false)
    @DisplayName("게시판 전체조회(페이징) 실패(클럽이 없을경우)")
    void getsNotice_Failure_Club(){

      //given
      NoticeRequestDto noticeRequestDto2 = new NoticeRequestDto("공지사항2", "공지사항입니다2.", "board", null);
      Notice testNotice2 = Notice.createNotice(noticeRequestDto2, testMember, testClub, NoticeCategory.BOARD);
      noticeRepository.save(testNotice2);
      PageableDto pageableDto = new PageableDto(1,3,"CreatedAt");


      //when
      BisException exception = assertThrows(BisException.class, ()-> {
        noticeService.getsNotice(10L, pageableDto);
      });


      //then
      assertEquals(ErrorCode.NOT_FOUND_CLUB, exception.getErrorCode());

    }

  }

  @Nested
  @DisplayName("게시판 신고")
  class createReportNotice{

    @Test
    @Rollback(value = false)
    @DisplayName("게시판 신고 성공")
    void createReportNotice_Success(){

      //given
      ReportRequestDto requestDto = new ReportRequestDto("신고 사유");


      //when
      noticeService.createReportNotice(testNotice.getId(), requestDto, testMember);


      //then
      List<ReportNotice> reportNoticeList = reportRepository.findAllByNoticeId(testNotice.getId());
      assertEquals(1, reportNoticeList.size());

    }

  }


}
