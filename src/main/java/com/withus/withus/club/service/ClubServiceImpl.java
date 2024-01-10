package com.withus.withus.club.service;

import com.withus.withus.club.dto.ClubRequestDto;
import com.withus.withus.club.dto.ClubResponseDto;
import com.withus.withus.club.dto.ReportClubRequestDto;
import com.withus.withus.club.dto.ReportClubResponseDto;
import com.withus.withus.club.entity.Club;
import com.withus.withus.club.entity.ReportClub;
import com.withus.withus.club.repository.ClubRepository;
import com.withus.withus.club.repository.ReportClubRepository;
import com.withus.withus.global.exception.BisException;
import com.withus.withus.global.exception.ErrorCode;
import com.withus.withus.member.entity.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {
    private final ClubRepository clubRepository;
    private final ReportClubRepository reportClubRepository;

    // 작성
    @Override
    public ClubResponseDto createClub(ClubRequestDto clubRequestDto, Member member) {
        LocalDateTime startTime = clubRequestDto.startTime();
        LocalDateTime endTime = clubRequestDto.endTime();
        Club club = Club.createClub(clubRequestDto, member, startTime, endTime);
        Club savedClub = clubRepository.save(club);
        return ClubResponseDto.createClubResponseDto(savedClub);
    }
    //조회
    @Override
    public ClubResponseDto getClub(Long clubId) {
        Club club = findClubById(clubId);
        return ClubResponseDto.createClubResponseDto(club);
    }

    @Transactional
    public ClubResponseDto updateClub(Long clubId, ClubRequestDto clubRequestDto, Member member) {
        Club club = verifyMember(clubId);
        club.update(clubRequestDto);
        return ClubResponseDto.createClubResponseDto(club);
    }

    @Override
    @Transactional
    public String deleteClub(Long clubId, Member member) {
        Club club = verifyMember(clubId);
        club.delete();
        return "Club delete successfully";
    }

    @Override
    public ReportClubResponseDto reportClub(Long clubId, ReportClubRequestDto reportClubRequestDto, Member member) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new BisException(ErrorCode.NOT_FOUND_CLUB));
        ReportClub reportClub = ReportClub.reportClub(reportClubRequestDto, member, club);
        reportClubRepository.save(reportClub);
        int clubReportCount = club.getReport();
        club.updateReport(clubReportCount + 1);
        clubRepository.save(club);
        return ReportClubResponseDto.createReportClubResponseDto(club,reportClub);
    }


    public Club findClubById(Long clubId) {
        return clubRepository.findById(clubId).
                orElseThrow(() -> new BisException(ErrorCode.NOT_FOUND_CLUB));

    }

    private Club verifyMember(Long clubId) {
        Club club = clubRepository.findByIsActiveAndId(true, clubId)
        .orElseThrow(() ->
                new BisException(ErrorCode.NOT_FOUND_CLUB)
        );
        return club;
    }
}

