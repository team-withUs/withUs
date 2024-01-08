package com.withus.withus.club.controller;

import com.withus.withus.club.dto.ClubRequestDto;
import com.withus.withus.club.dto.ClubResponseDto;
import com.withus.withus.club.service.ClubService;
import com.withus.withus.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs")
public class ClubController {
    private final ClubService clubService;

    // 작성
    @PostMapping
    public ResponseEntity<ClubResponseDto> createBoard(@RequestBody ClubRequestDto clubRequestDto,
                                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ClubResponseDto responseDto = clubService.createClub(clubRequestDto, userDetails.getMember());
        return ResponseEntity.status(201).body(responseDto);
    }
}
