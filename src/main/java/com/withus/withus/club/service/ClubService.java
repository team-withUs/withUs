package com.withus.withus.club.service;

import com.withus.withus.club.dto.ClubRequestDto;
import com.withus.withus.club.dto.ClubResponseDto;

import com.withus.withus.club.entity.Club;

import com.withus.withus.member.entity.Member;

public interface ClubService {

    ClubResponseDto createClub(ClubRequestDto clubRequestDto, Member member);

    ClubResponseDto getClub(Long clubId);
    
    ClubResponseDto updateClub(Long clubId, ClubRequestDto clubRequestDto, Member member);

    String deleteClub(Long clubId, Member member);

    ReportClubResponseDto createReportClub(Long clubId, ReportClubRequestDto reportClubRequestDto, Member member);

}
