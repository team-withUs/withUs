package com.withus.withus.club.controller;

import com.withus.withus.club.dto.ClubRequestDto;
import com.withus.withus.club.dto.ClubResponseDto;
import com.withus.withus.club.service.ClubService;
import com.withus.withus.global.annotation.AuthMember;
import com.withus.withus.global.response.CommonResponse;
import com.withus.withus.global.response.ResponseCode;
import com.withus.withus.global.security.UserDetailsImpl;
import com.withus.withus.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/club")
public class ClubController {
    private final ClubService clubService;

    // 작성
    @PostMapping
    public ResponseEntity<CommonResponse<ClubResponseDto>> createBoard(
            @RequestBody ClubRequestDto clubRequestDto,
            @AuthMember Member member
    ) {
        ClubResponseDto responseDto = clubService.createClub(clubRequestDto, member);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_CLUB_CREATE.getHttpStatus())
                .body(CommonResponse.of(ResponseCode.SUCCESS_CLUB_CREATE,responseDto));
    }

    @GetMapping("/{clubId}")
    public ResponseEntity<CommonResponse<ClubResponseDto>> getClub(
            @PathVariable("clubId") Long clubId
    ) {
        ClubResponseDto responseDto = clubService.getClub(clubId);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_CLUB_GET.getHttpStatus())
                .body(CommonResponse.of(ResponseCode.SUCCESS_CLUB_GET, responseDto));
    }

    //수정
    @PatchMapping("/{clubId}")
    public ResponseEntity<CommonResponse> updateClub(
            @PathVariable("clubId") Long clubId,
            @RequestBody ClubRequestDto clubRequestDto,
            @AuthMember Member member
    ) {
        ClubResponseDto responseDto = clubService.updateClub(clubId, clubRequestDto, member);
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
    @PatchMapping("/report/{clubId}")
    public ResponseEntity<CommonResponse<String>> updateReportNotice(
            @PathVariable("clubId") Long clubId
    ) {
        clubService.updateReportClub(clubId);
        return ResponseEntity.status(ResponseCode.SUCCESS_CLUB_REPORT.getHttpStatus())
                .body(CommonResponse.of(ResponseCode.SUCCESS_CLUB_REPORT,""));
    }
}
