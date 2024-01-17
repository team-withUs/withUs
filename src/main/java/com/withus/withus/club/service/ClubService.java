package com.withus.withus.club.service;

import com.withus.withus.category.entity.ClubCategory;
import com.withus.withus.club.dto.ClubRequestDto;
import com.withus.withus.club.dto.ClubResponseDto;

import com.withus.withus.club.dto.ReportClubRequestDto;
import com.withus.withus.club.dto.ReportClubResponseDto;
import com.withus.withus.club.entity.Club;

import com.withus.withus.member.entity.Member;
import com.withus.withus.notice.dto.PageableDto;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ClubService {

    ClubResponseDto createClub(ClubRequestDto clubRequestDto, Member member, MultipartFile image);

    ClubResponseDto getClub(Long clubId);

    ClubResponseDto updateClub(Long clubId, ClubRequestDto clubRequestDto, Member member, MultipartFile image);


    String deleteClub(Long clubId, Member member);


    Club findClubById(Long clubId);

    ReportClubResponseDto createReportClub(Long clubId, ReportClubRequestDto reportClubRequestDto, Member member);

    List<ClubResponseDto> getsClubByCategory(ClubCategory category, PageableDto pageableDto);

    Integer count();

}
