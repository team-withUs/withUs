package com.withus.withus.domain.club.service;

import com.withus.withus.domain.club.entity.ClubCategory;
import com.withus.withus.domain.club.dto.ClubRequestDto;
import com.withus.withus.domain.club.dto.ClubResponseDto;

import com.withus.withus.domain.club.dto.ReportClubRequestDto;
import com.withus.withus.domain.club.dto.ReportClubResponseDto;
import com.withus.withus.domain.club.entity.Club;

import com.withus.withus.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ClubService {

    ClubResponseDto createClub(ClubRequestDto clubRequestDto, Member member, MultipartFile image);

    ClubResponseDto getClub(Long clubId);

    ClubResponseDto updateClub(Long clubId, ClubRequestDto clubRequestDto, Member member, MultipartFile image);


    String deleteClub(Long clubId, Member member);


    Club findClubById(Long clubId);

    ReportClubResponseDto createReportClub(Long clubId, ReportClubRequestDto reportClubRequestDto, Member member);

    Page<ClubResponseDto> getsClubByCategory(ClubCategory category, Pageable pageable, String keyWord, String searchCategory);

    Integer count();

    List<ClubResponseDto> getAllClubs();

}
