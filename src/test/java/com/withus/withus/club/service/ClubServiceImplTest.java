package com.withus.withus.club.service;

import com.withus.withus.domain.club.entity.ClubCategory;
import com.withus.withus.domain.club.dto.ClubRequestDto;
import com.withus.withus.domain.club.dto.ClubResponseDto;
import com.withus.withus.domain.club.dto.ReportClubRequestDto;
import com.withus.withus.domain.club.dto.ReportClubResponseDto;
import com.withus.withus.domain.club.entity.Club;
import com.withus.withus.domain.club.repository.ClubRepository;
import com.withus.withus.domain.club.repository.ReportClubRepository;
import com.withus.withus.domain.club.service.ClubServiceImpl;
import com.withus.withus.global.exception.BisException;
import com.withus.withus.global.exception.ErrorCode;
import com.withus.withus.domain.member.entity.Member;
import com.withus.withus.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ClubServiceImplTest {

    @Autowired
    private ClubServiceImpl clubService;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ReportClubRepository reportClubRepository;

    private Member member;
    private Long member_id;
    Club club;

    @BeforeEach
    void createInfo() {
        clubRepository.deleteAll();
        reportClubRepository.deleteAll();
        memberRepository.deleteAll();

        Member savedMember = memberRepository.save(Member.builder()
                .loginname("testlogin")
                .username("testUser")
                .password("password")
                .email("email@email.com")
                .build()
        );
        member_id = savedMember.getId();

        createSampleClub();
    }

    private ClubResponseDto createSampleClub() {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("imageFile", "test-image.jpg", "image/jpeg", "image content".getBytes());

        ClubRequestDto clubRequestDto = new ClubRequestDto(
                "clubTitle",
                "content",
                ClubCategory.SPORTS,
                mockMultipartFile,
                LocalDateTime.parse("2024-01-07T09:00:00"),
                LocalDateTime.parse("2024-02-08T09:00:00"));

        member = memberRepository.findById(member_id).orElse(null);

        return clubService.createClub(clubRequestDto, member, mockMultipartFile);
    }


    @Nested
    @DisplayName("Club_Create_Test")
    class ClubCreationTests {

        @Test
        @DisplayName("Create_Club (성공)")
        void createClub_Success() {
            // GIVEN
            MockMultipartFile mockMultipartFile = new MockMultipartFile("imageFile", "test-image.jpg", "image/jpeg", "image content".getBytes());

            ClubRequestDto clubRequestDto = new ClubRequestDto(
                    "clubTitle",
                    "content",
                    ClubCategory.SPORTS,
                    mockMultipartFile,
                    LocalDateTime.parse("2024-01-07T09:00:00"),
                    LocalDateTime.parse("2024-02-08T09:00:00"));

            member = memberRepository.findById(member_id).orElse(null);

            // WHEN
            ClubResponseDto createClub = clubService.createClub(clubRequestDto, member, clubRequestDto.imageFile());

            // THEN
            assertNotNull(createClub);
            assertEquals("clubTitle", createClub.clubTitle());
            assertEquals("content", createClub.content());
            assertEquals(ClubCategory.SPORTS, createClub.category());
            assertEquals(LocalDateTime.parse("2024-01-07T09:00:00"), createClub.startTime());
            assertEquals(LocalDateTime.parse("2024-02-08T09:00:00"), createClub.endTime());
        }

        @Test
        @DisplayName("CreateClub_Fail (클럽 제목 없이 생성 Test)")
        void createClub_Fail_Title() {
            // GIVEN
            MockMultipartFile mockMultipartFile = new MockMultipartFile("imageFile", "test-image.jpg", "image/jpeg", "image content".getBytes());

            ClubRequestDto clubRequestDto = new ClubRequestDto(
                    "",
                    "content",
                    ClubCategory.SPORTS,
                    mockMultipartFile,
                    LocalDateTime.parse("2024-01-07T09:00:00"),
                    LocalDateTime.parse("2024-02-08T09:00:00"));

            member = memberRepository.findById(member_id).orElse(null);

            // WHEN & THEN
            assertThrows(BisException.class, () -> {
                ClubResponseDto club = clubService.createClub(clubRequestDto, member, mockMultipartFile);
            });
        }

        @Test
        @DisplayName("CreateClub_Fail (시작 시간이 없는 Test)")
        void createClub_Fail_EmptyStartTime() {
            // GIVEN
            MockMultipartFile mockMultipartFile = new MockMultipartFile("imageFile", "test-image.jpg", "image/jpeg", "image content".getBytes());

            ClubRequestDto clubRequestDto = new ClubRequestDto(
                    "clubTitle",
                    "content",
                    ClubCategory.SPORTS,
                    mockMultipartFile,
                    null,
                    LocalDateTime.parse("2024-02-08T09:00:00"));

            member = memberRepository.findById(member_id).orElse(null);

            // WHEN & THEN
            assertThrows(BisException.class, () -> {
                ClubResponseDto club = clubService.createClub(clubRequestDto, member, mockMultipartFile);
            });
        }

        @Test
        @DisplayName("CreateClub_Fail (마감 시간 없는 Test)")
        void createClub_Fail_EmptyEndTime() {
            // GIVEN
            MockMultipartFile mockMultipartFile = new MockMultipartFile("imageFile", "test-image.jpg", "image/jpeg", "image content".getBytes());

            ClubRequestDto clubRequestDto = new ClubRequestDto(
                    "clubTitle",
                    "content",
                    ClubCategory.SPORTS,
                    mockMultipartFile,
                    LocalDateTime.parse("2024-02-08T09:00:00"),
            null);

            member = memberRepository.findById(member_id).orElse(null);

            // WHEN & THEN
            assertThrows(BisException.class, () -> {
                ClubResponseDto club = clubService.createClub(clubRequestDto, member, mockMultipartFile);
            });
        }

    }

    @Nested
    @DisplayName("Club_Get_Test")
    class ClubGetTest {
        @Test
        @DisplayName("Club_Get (성공)")
        void getClubById() {
            // GIVEN
            ClubResponseDto expectedClub = createSampleClub();

            // WHEN
            ClubResponseDto actualClub = clubService.getClub(expectedClub.clubId());

            // THEN
            assertNotNull(actualClub);
            assertEquals(expectedClub.clubId(), actualClub.clubId());
            assertEquals(expectedClub.clubTitle(), actualClub.clubTitle());
            assertEquals(expectedClub.content(), actualClub.content());
            assertEquals(expectedClub.category(), actualClub.category());
            assertEquals(expectedClub.imageURL(), actualClub.imageURL());
            assertEquals(expectedClub.startTime(), actualClub.startTime());
            assertEquals(expectedClub.endTime(), actualClub.endTime());
        }

        @Test
        @DisplayName("clubByIdFail (클럽이 없는 Test)")
        void getClubById_Fail() {
            // GIVEN
            Long nonExistentClubId = Long.MAX_VALUE;

            // WHEN & THEN
            assertThrows(BisException.class, () -> clubService.getClub(nonExistentClubId));
        }
    }

    @Nested
    @DisplayName("Club_Update_Test")
    class updateClubTest {
        @Test
        @DisplayName("Club_Update (성공)")
        void updateClub_Success() {

            // GIVEN
            MockMultipartFile mockMultipartFile = new MockMultipartFile("imageFile", "test-image.jpg", "image/jpeg", "updated image content".getBytes());

            ClubResponseDto initialClub = createSampleClub();
            Long clubId = initialClub.clubId();

            ClubRequestDto clubRequestDto = new ClubRequestDto(
                    "UpdatedTitle",
                    "UpdatedContent",
                    ClubCategory.ETC,
                    mockMultipartFile,
                    LocalDateTime.parse("2024-01-07T10:00:00"),
                    LocalDateTime.parse("2024-02-08T10:00:00"));

            // WHEN
            ClubResponseDto updatedClub = clubService.updateClub(clubId, clubRequestDto, member, clubRequestDto.imageFile());

            // THEN
            assertNotNull(updatedClub);
            assertEquals(clubId, updatedClub.clubId());
            assertEquals("UpdatedTitle", updatedClub.clubTitle());
            assertEquals("UpdatedContent", updatedClub.content());
            assertEquals(ClubCategory.ETC, updatedClub.category());
            assertFalse(updatedClub.imageURL().startsWith("https://your-s3-bucket.s3.amazonaws.com/"));
            assertEquals(updatedClub.startTime(), clubRequestDto.startTime());
            assertEquals(updatedClub.endTime(), clubRequestDto.endTime());
        }

        @Test
        @DisplayName("Club_Update_Fail (없는 클럽 수정 불가 Test)")
        void updateClub_Fail_ClubNotExists() {
            // GIVEN
            Long nonExistingClubId = 999L;
            MockMultipartFile mockMultipartFile = new MockMultipartFile("imageFile", "test-image.jpg", "image/jpeg", "updated image content".getBytes());
            ClubRequestDto clubRequestDto = new ClubRequestDto(
                    "UpdatedTitle",
                    "UpdatedContent",
                    ClubCategory.ETC,
                    mockMultipartFile,
                    LocalDateTime.parse("2024-01-07T10:00:00"),
                    LocalDateTime.parse("2024-02-08T10:00:00"));
            // WHEN & THEN
            assertThrows(BisException.class, () -> {
                clubService.updateClub(nonExistingClubId, clubRequestDto, member, clubRequestDto.imageFile());
            });
        }
    }

    @Nested
    @DisplayName("Club_Delete_Test")
    class deleteClubTest {
        @Test
        @Rollback(value = false)
        @DisplayName("ClubDelete (성공)")
        void deleteClub_Success() {
            // GIVEN
            ClubResponseDto clubToDelete = createSampleClub();
            Long clubId = clubToDelete.clubId();

            // WHEN
            String result = clubService.deleteClub(clubId, member);

            // THEN
            assertFalse(clubRepository.findById(clubId).get().isActive());
        }

        @Test
        @DisplayName("Club_Delete_Fail (존재하지 않는 클럽 삭제 실패 Test)")
        void deleteClub_Fail_NotFound() {
            // GIVEN
            Long nonExistingClubId = 999L;

            // WHEN & THEN
            BisException exception = assertThrows(BisException.class, () -> {
                clubService.deleteClub(nonExistingClubId, member);
            });

            assertEquals(ErrorCode.NOT_FOUND_CLUB, exception.getErrorCode());
        }
    }

    @Nested
    @DisplayName("Club_Category_Paging_Test")
    class ClubCategoryPagingTest {

        @Test
        @DisplayName("Get_Clubs_By_Category_paging (성공)")
        void getsClubByCategoryPaging_Success() {
            // GIVEN
            ClubCategory category = ClubCategory.SPORTS;
            Pageable pageable = PageRequest.of(1, 5, Sort.by("createdAt"));
            // WHEN
            String keyWord = "ace245";
            String searchCategory = "";
            Page<ClubResponseDto> clubList = clubService.getsClubByCategory(category, pageable, keyWord, searchCategory);

            // THEN
            assertNotNull(clubList);
        }

        @Test
        @DisplayName("Get_Clubs_By_Category_paging (페이징 실패)")
        void getsClubByCategoryPaging_Failure() {
            // GIVEN
            ClubCategory category = ClubCategory.SPORTS;

            int pageNumber = -1;
            int pageSize = 5;

            // WHEN & THEN
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt"));
                clubService.getsClubByCategory(category, pageable, "ace245", "");
            });

            assertEquals("Page index must not be less than zero", exception.getMessage());
        }
    };

    @Nested
    @DisplayName("Report_Test")
    class ReportClubTest {

        @Test
        @DisplayName("Club_Report (성공)")
        void createReportClub_Success() {
            // GIVEN
            ClubResponseDto clubToReport = createSampleClub();
            Long clubId = clubToReport.clubId();

            ReportClubRequestDto reportClubRequestDto = ReportClubRequestDto.builder()
                    .content("규칙 위반 합니다.")
                    .build();

            // WHEN
            ReportClubResponseDto reportClubResponseDto = clubService.createReportClub(clubId, reportClubRequestDto, member);

            // THEN
            assertNotNull(reportClubResponseDto);
            assertEquals(clubId, reportClubResponseDto.clubId());
            assertEquals("testUser", reportClubResponseDto.username());
            assertEquals("규칙 위반 합니다.", reportClubResponseDto.content());
        }

        @Test
        @DisplayName("Report_Club_Fail (없는 클럽에 신고 Test)")
        void createReportClub_Fail_ClubNotFound() {
            // GIVEN
            Long nonExistingClubId = Long.MAX_VALUE;

            ReportClubRequestDto reportClubRequestDto = ReportClubRequestDto.builder()
                    .content("규칙 위반 합니다.")
                    .build();

            // WHEN & THEN
            BisException exception = assertThrows(BisException.class, () -> {
                clubService.createReportClub(nonExistingClubId, reportClubRequestDto, member);
            });

            assertEquals(ErrorCode.NOT_FOUND_CLUB, exception.getErrorCode());
        }

        @Test
        @DisplayName("Report_Club_Fail (빈 내용 Test)")
        void createReportClub_Fail_EmptyContent() {
            // GIVEN
            ClubResponseDto clubToReport = createSampleClub();
            Long clubId = clubToReport.clubId();

            ReportClubRequestDto reportClubRequestDto = ReportClubRequestDto.builder()
                    .content("")
                    .build();

            // WHEN & THEN
            assertThrows(BisException.class, () -> clubService.createReportClub(clubId, reportClubRequestDto, member));
        }

        @Test
        @DisplayName("Report_Club_Fail (여러번 신고 Test)")
        void createReportClub_Fail_Repeated() {
            // GIVEN
            ClubResponseDto clubToReport = createSampleClub();
            Long clubId = clubToReport.clubId();

            ReportClubRequestDto reportClubRequestDto = ReportClubRequestDto.builder()
                    .content("규칙 위반 합니다.")
                    .build();

            clubService.createReportClub(clubId, reportClubRequestDto, member);

            // WHEN & THEN
            assertThrows(BisException.class, () -> {
                clubService.createReportClub(clubId, reportClubRequestDto, member);
            });

        }
    }
}