package com.withus.withus.club.controller;

import com.withus.withus.club.dto.ClubRequestDto;
import com.withus.withus.club.dto.ClubResponseDto;
import com.withus.withus.club.service.ClubService;
import com.withus.withus.global.response.CommonResponse;
import com.withus.withus.global.response.ResponseCode;
import com.withus.withus.global.security.UserDetailsImpl;
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
    public ResponseEntity<CommonResponse<ClubResponseDto>> createBoard(@RequestBody ClubRequestDto clubRequestDto,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ClubResponseDto responseDto = clubService.createClub(clubRequestDto, userDetails.getMember());
        return ResponseEntity
                .status(ResponseCode.SUCCESS_CLUB_CREATE.getHttpStatus())
                .body(CommonResponse.of(ResponseCode.SUCCESS_CLUB_CREATE,responseDto));
    }

    //조회
    @ResponseBody
    @GetMapping("/{clubId}")
    public ClubResponseDto getClub(@PathVariable("clubId") Long clubId) {
        return clubService.getClub(clubId);
    }

    //수정
    @PatchMapping("/{clubId}")
    public ResponseEntity<CommonResponse> updateClub(@PathVariable("clubId") Long clubId,
                                                     @RequestBody ClubRequestDto clubRequestDto,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ClubResponseDto responseDto = clubService.updateClub(clubId, clubRequestDto, userDetails);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_CLUB_UPDATE.getHttpStatus())
                .body(CommonResponse.of(ResponseCode.SUCCESS_CLUB_UPDATE, responseDto));
    }

    //삭제
    @DeleteMapping("/{clubId}")
    public ResponseEntity<CommonResponse<String>> deleteClub(@PathVariable("clubId") Long clubId,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails){
        String responseDto = clubService.deleteClub(clubId, userDetails);
        CommonResponse<String> commonResponse = CommonResponse.of(ResponseCode.SUCCESS_CLUB_DELETE, responseDto);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_CLUB_DELETE.getHttpStatus())
                .body(commonResponse);
    }

}
