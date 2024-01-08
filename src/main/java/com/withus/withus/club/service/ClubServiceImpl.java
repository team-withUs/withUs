package com.withus.withus.club.service;

import com.withus.withus.club.dto.ClubRequestDto;
import com.withus.withus.club.dto.ClubResponseDto;
import com.withus.withus.club.entity.Club;
import com.withus.withus.club.repository.ClubRepository;
import com.withus.withus.global.security.UserDetailsImpl;
import com.withus.withus.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {
    private final ClubRepository clubRepository;


    @Override
    public ClubResponseDto createClub(ClubRequestDto clubrequestDto, Member member) {
        LocalDateTime startTime = clubrequestDto.startTime(); // Assuming getStartTime() returns LocalDateTime
        LocalDateTime endTime = clubrequestDto.endTime(); // Assuming getEndTime() returns LocalDateTime

        Club club = new Club(clubrequestDto, member, startTime, endTime);
        club.setMember(member);

        Club savedClub = clubRepository.save(club);
        return new ClubResponseDto(savedClub);
    }

    @Override
    public ClubResponseDto getClub(Long clubId) {
        return new ClubResponseDto(findClubById(clubId));
    }
    private Club findClubById(Long clubId) {
        return clubRepository.findById(clubId).
                orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ClubId입니다."));

    }


    @Override
    @Transactional
    public ClubResponseDto updateClub(Long clubId, ClubRequestDto clubrequestDto, UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        Club club = verifyMember(member,clubId);
        club.update(clubrequestDto);

        return new ClubResponseDto(club);
    }

    private Club verifyMember(Member member, Long clubId) {
        Club club = findClubById(clubId);
        if(!club.getMember().getUsername().equals(member.getUsername())){
            throw new IllegalArgumentException("해당 사용자가 아닙니다.");
        }
        return club;
    }

}
