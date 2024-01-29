package com.withus.withus.domain.club.controller;

import com.withus.withus.domain.club.entity.ClubCategory;
import com.withus.withus.domain.club.dto.ClubRequestDto;
import com.withus.withus.domain.club.dto.ClubResponseDto;
import com.withus.withus.domain.club.dto.InviteMemberResponseDto;
import com.withus.withus.domain.club.dto.ReportClubRequestDto;
import com.withus.withus.domain.club.dto.ReportClubResponseDto;
import com.withus.withus.domain.club.entity.ClubMember;
import com.withus.withus.domain.club.service.ClubMemberService;
import com.withus.withus.domain.club.service.ClubService;
import com.withus.withus.global.annotation.AuthMember;
import com.withus.withus.global.response.CommonResponse;
import com.withus.withus.global.response.ResponseCode;
import com.withus.withus.domain.member.entity.Member;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/club")
public class ClubController {

    private final ClubService clubService;

    private final ClubMemberService clubMemberService;

    @PostMapping
    public ResponseEntity<CommonResponse<ClubResponseDto>> createClub(
            @ModelAttribute ClubRequestDto clubRequestDto,
            @AuthMember Member member
    ) {
        ClubResponseDto responseDto = clubService.createClub(
            clubRequestDto, member, clubRequestDto.imageFile()
        );

        return ResponseEntity
                .status(ResponseCode.SUCCESS_CLUB_CREATE.getHttpStatus())
                .body(CommonResponse.of(ResponseCode.SUCCESS_CLUB_CREATE, responseDto));
    }


    // 클럽에 초대된 유저조회
    @GetMapping("/{clubId}/inviteList")
    public ResponseEntity<CommonResponse<List<InviteMemberResponseDto>>> getInvitedUserByClub(
            @PathVariable("clubId") Long clubId
    ) {
        List<ClubMember> invitedClubMembers = clubMemberService.getInvitedUserByClub(clubId);
        List<InviteMemberResponseDto> invitedUsernames = invitedClubMembers.stream()
                .map(InviteMemberResponseDto::createInviteMemberResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity
                .status(ResponseCode.OK.getHttpStatus())
                .body(CommonResponse.of(ResponseCode.OK, invitedUsernames));

    }

    @GetMapping("/{clubId}/memberCount")
    public ResponseEntity<CommonResponse<Integer>> getClubMemberCount(
            @PathVariable("clubId") Long clubId
    ) {
        int memberCount = clubMemberService.getClubMemberCount(clubId);

        return ResponseEntity
                .status(ResponseCode.OK.getHttpStatus())
                .body(CommonResponse.of(ResponseCode.OK, memberCount));
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<ClubResponseDto>>> getAllClubs() {
        List<ClubResponseDto> responseDtoList = clubService.getAllClubs();

        return ResponseEntity
                .status(ResponseCode.SUCCESS_CLUB_GET.getHttpStatus())
                .body(CommonResponse.of(ResponseCode.SUCCESS_CLUB_GET, responseDtoList));
    }

    //조회
    @GetMapping("/{clubId}")
    public ResponseEntity<CommonResponse<ClubResponseDto>> getClub(
            @PathVariable("clubId") Long clubId
    ) {
        ClubResponseDto responseDto = clubService.getClub(clubId);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_CLUB_GET.getHttpStatus())
                .body(CommonResponse.of(ResponseCode.SUCCESS_CLUB_GET, responseDto));
    }

    // 수정
    @PatchMapping("/{clubId}")
    public ResponseEntity<CommonResponse> updateClub(
            @PathVariable("clubId") Long clubId,
            @ModelAttribute ClubRequestDto clubRequestDto,
            @AuthMember Member member
    ) {
        ClubResponseDto responseDto = clubService.updateClub(
            clubId, clubRequestDto, member, clubRequestDto.imageFile()
        );

        return ResponseEntity
                .status(ResponseCode.SUCCESS_CLUB_UPDATE.getHttpStatus())
                .body(CommonResponse.of(ResponseCode.SUCCESS_CLUB_UPDATE, responseDto));
    }

    //삭제
    @DeleteMapping("/{clubId}")
    public ResponseEntity<CommonResponse<String>> deleteClub(
            @PathVariable("clubId") Long clubId,
            @AuthMember Member member
    ) {
        String responseDto = clubService.deleteClub(clubId, member);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_CLUB_DELETE.getHttpStatus())
                .body(CommonResponse.of(ResponseCode.SUCCESS_CLUB_DELETE, responseDto));
    }

    // 신고
    @PostMapping("/{clubId}/report")
    public ResponseEntity<CommonResponse<ReportClubResponseDto>> reportClub(
            @PathVariable("clubId") Long clubId,
            @RequestBody ReportClubRequestDto reportClubRequestDto,
            @AuthMember Member member
    ) {
        ReportClubResponseDto reportClubResponse = clubService.createReportClub(
            clubId, reportClubRequestDto, member
        );

        return ResponseEntity
                .status(ResponseCode.SUCCESS_CLUB_REPORT.getHttpStatus())
                .body(CommonResponse.of(ResponseCode.SUCCESS_CLUB_REPORT, reportClubResponse));
    }

    // 카테고리
    @GetMapping("/{category}/club")
    public ResponseEntity<CommonResponse<Page<ClubResponseDto>>> getsClub(
            @PathVariable("category") ClubCategory category,
            Pageable pageable,
            @RequestParam(value = "keyWord", defaultValue = "ace245") String keyWord,
            @RequestParam(value = "searchCategory", defaultValue = "all") String searchCategory
    ) {

        return ResponseEntity
            .status(ResponseCode.OK.getHttpStatus())
            .body(CommonResponse.of(ResponseCode.OK, clubService.getsClubByCategory(
                category, pageable, keyWord,searchCategory
            )));
    }

    // 클럽 멤버 탈퇴
    @DeleteMapping("/{clubId}/leave")
    public ResponseEntity<CommonResponse<String>> leaveClub(
            @PathVariable("clubId") Long clubId,
            @AuthMember Member member
    ) {
        String responseDto = clubMemberService.leaveClub(clubId, member);

        return ResponseEntity
                .status(ResponseCode.OK.getHttpStatus())
                .body(CommonResponse.of(ResponseCode.OK, responseDto));
    }
}