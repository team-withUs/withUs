package com.withus.withus.club.controller;

import com.withus.withus.category.entity.ClubCategory;
import com.withus.withus.club.dto.ClubRequestDto;
import com.withus.withus.club.dto.ClubResponseDto;
import com.withus.withus.club.dto.ReportClubRequestDto;
import com.withus.withus.club.dto.ReportClubResponseDto;
import com.withus.withus.club.service.ClubService;
import com.withus.withus.global.annotation.AuthMember;
import com.withus.withus.global.response.CommonResponse;
import com.withus.withus.global.response.ResponseCode;
import com.withus.withus.member.entity.Member;
import com.withus.withus.notice.dto.PageableDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/club")
public class ClubController {
    private final ClubService clubService;

    @PostMapping
    public ResponseEntity<CommonResponse<ClubResponseDto>> createClub(
            @ModelAttribute ClubRequestDto clubRequestDto,
            @AuthMember Member member
    ) {
        System.out.println(clubRequestDto);
        ClubResponseDto responseDto = clubService.createClub(clubRequestDto, member, clubRequestDto.imageFile());
        return ResponseEntity
                .status(ResponseCode.SUCCESS_CLUB_CREATE.getHttpStatus())
                .body(CommonResponse.of(ResponseCode.SUCCESS_CLUB_CREATE, responseDto));
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
            @RequestParam(value = "sortBy",defaultValue = "CreatedAt") String sortBy,
            @RequestParam(value = "keyWord" ,defaultValue = "ace245") String keyWord
    ) {
        PageableDto pageableDto = new PageableDto(page, size, sortBy);


        return ResponseEntity.status(ResponseCode.OK.getHttpStatus())
            .body(CommonResponse.of(ResponseCode.OK,
                clubService.getsClubByCategory(category, pageableDto, keyWord)));
    }

}
