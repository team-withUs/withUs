package com.withus.withus.club.controller;

import com.withus.withus.category.entity.ClubCategory;
import com.withus.withus.club.dto.ClubRequestDto;
import com.withus.withus.club.dto.ClubResponseDto;
import com.withus.withus.club.dto.ReportClubRequestDto;
import com.withus.withus.club.dto.ReportClubResponseDto;
import com.withus.withus.club.entity.ClubMember;
import com.withus.withus.club.service.ClubMemberService;
import com.withus.withus.club.service.ClubMemberServiceImpl;
import com.withus.withus.club.service.ClubService;
import com.withus.withus.global.annotation.AuthMember;
import com.withus.withus.global.exception.ErrorCode;
import com.withus.withus.global.response.CommonResponse;
import com.withus.withus.global.response.ResponseCode;
import com.withus.withus.member.entity.Member;
import com.withus.withus.member.service.MemberService;
import com.withus.withus.notice.dto.PageableDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/club")
public class ClubController {
    private final ClubService clubService;
    private final MemberService memberService;
    private final ClubMemberService clubMemberService;

    @PostMapping
    public ResponseEntity<CommonResponse<ClubResponseDto>> createClub(
            @ModelAttribute ClubRequestDto clubRequestDto,
            @AuthMember Member member
    ) {
        ClubResponseDto responseDto = clubService.createClub(clubRequestDto, member, clubRequestDto.imageFile());
        return ResponseEntity
                .status(ResponseCode.SUCCESS_CLUB_CREATE.getHttpStatus())
                .body(CommonResponse.of(ResponseCode.SUCCESS_CLUB_CREATE, responseDto));
    }

    // 클럽에 초대된 유저조회
    @GetMapping("/{clubId}/inviteList")
    public ResponseEntity<CommonResponse<List<String>>> getInvitedUserByClub(
            @PathVariable Long clubId
    ) {
        List<ClubMember> invitedClubMembers = clubMemberService.getInvitedUserByClub(clubId);
        List<String> invitedUsernames = invitedClubMembers.stream()
                .map(clubMember -> clubMember.getMember().getUsername())
                .collect(Collectors.toList());

        return ResponseEntity
                .status(ResponseCode.OK.getHttpStatus())
                .body(CommonResponse.of(ResponseCode.OK, invitedUsernames));

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
        ClubResponseDto responseDto = clubService.updateClub(clubId, clubRequestDto, member, clubRequestDto.imageFile());
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
        ReportClubResponseDto reportClubResponse = clubService.createReportClub(clubId, reportClubRequestDto, member);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_CLUB_REPORT.getHttpStatus())
                .body(CommonResponse.of(ResponseCode.SUCCESS_CLUB_REPORT, reportClubResponse));
    }

    // 카테고리
    @GetMapping("/{category}/club")
    public ResponseEntity<CommonResponse<List<ClubResponseDto>>> getsClub(
            @PathVariable("category") ClubCategory category,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "4") int size,
            @RequestParam(value = "sortBy", defaultValue = "CreatedAt") String sortBy,
            @RequestParam(value = "keyWord", defaultValue = "ace245") String keyWord
    ) {
        PageableDto pageableDto = new PageableDto(page, size, sortBy);
        return ResponseEntity.status(ResponseCode.OK.getHttpStatus())
                .body(CommonResponse.of(ResponseCode.OK,
                        clubService.getsClubByCategory(category, pageableDto, keyWord)));
    }
}