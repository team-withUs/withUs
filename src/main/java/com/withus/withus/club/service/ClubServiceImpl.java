package com.withus.withus.club.service;

import com.withus.withus.club.dto.ClubRequestDto;
import com.withus.withus.club.dto.ClubResponseDto;
import com.withus.withus.club.entity.Club;
import com.withus.withus.club.repository.ClubRepository;
import com.withus.withus.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {
    private final ClubRepository clubRepository;


    @Override
    public ClubResponseDto createClub(ClubRequestDto clubRequestDto, Member member) {
        LocalDateTime startTime = clubRequestDto.startTime(); // Assuming getStartTime() returns LocalDateTime
        LocalDateTime endTime = clubRequestDto.endTime(); // Assuming getEndTime() returns LocalDateTime

        Club club = new Club(clubRequestDto, member, startTime, endTime);
        club.setMember(member);

        Club savedClub = clubRepository.save(club);
        return new ClubResponseDto(savedClub);
    }

    @Override
    public ClubResponseDto getClub(Long clubId) {
        return new ClubResponseDto(findClubById(clubId));
    }

    private Club findClubById(Long clubId) {
        return clubRepository.findById(clubId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ClubId입니다."));

    }
}
