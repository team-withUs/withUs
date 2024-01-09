package com.withus.withus.club.service;

import com.withus.withus.club.dto.ClubRequestDto;
import com.withus.withus.club.dto.ClubResponseDto;
import com.withus.withus.club.entity.Club;
import com.withus.withus.club.repository.ClubRepository;
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

    // 수정
    @Transactional
    public ClubResponseDto updateClub(Long clubId, ClubRequestDto clubRequestDto, Member member) {
        Club club = verifyMember(member, clubId);
        club.update(clubRequestDto);
        Club updatedClub = clubRepository.save(club);
        return ClubResponseDto.createClubResponseDto(updatedClub);
    }

    // 삭제
    @Transactional
    public String deleteClub(Long clubId, Member member) {
        Club club = verifyMember(member, clubId);
        clubRepository.delete(club);
        return "Club deleted successfully";
    }

    // 신고
    @Transactional
    @Override
    public void updateReportClub(Long clubId) {
        Club club = findByIsActiveAndClubId(clubId);
        club.updateReport(club.getReport() + 1);
        if (club.getReport() >= 3) {
            club.delete();
        }
    }

    public Club findByIsActiveAndClubId(Long clubId) {
        Club club = clubRepository.findByIsActiveAndId(true, clubId)
                .orElseThrow(() ->
                        new BisException(ErrorCode.NOT_FOUND_CLUB)
                );
        return club;
    }
    private Club findClubById(Long clubId) {
        return clubRepository.findById(clubId).
                orElseThrow(() -> new BisException(ErrorCode.NOT_FOUND_CLUB));

    }

    private Club verifyMember(Member member, Long clubId) {
        Club club = findClubById(clubId);
        if (!club.getMember().getUsername().equals(member.getUsername())) {
            throw new BisException(ErrorCode.NOT_FOUND_MEMBER);
        }
        return club;
    }
}

