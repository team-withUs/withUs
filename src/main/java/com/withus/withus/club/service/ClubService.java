package com.withus.withus.club.service;

import com.withus.withus.club.dto.ClubRequestDto;
import com.withus.withus.club.dto.ClubResponseDto;
import com.withus.withus.global.security.UserDetailsImpl;
import com.withus.withus.member.entity.Member;

public interface ClubService {
    ClubResponseDto createClub(ClubRequestDto clubRequestDto, Member member);

    ClubResponseDto getClub(Long clubId);

    ClubResponseDto updateClub(Long clubId, ClubRequestDto clubRequestDto, UserDetailsImpl userDetails);

    String deleteClub(Long clubId, UserDetailsImpl userDetails);
}
