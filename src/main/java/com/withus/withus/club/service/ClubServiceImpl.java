package com.withus.withus.club.service;

import com.withus.withus.club.dto.ClubRequestDto;
import com.withus.withus.club.dto.ClubResponseDto;
import com.withus.withus.club.entity.Club;
import com.withus.withus.club.repository.ClubRepository;
import com.withus.withus.global.exception.BisException;
import com.withus.withus.global.exception.ErrorCode;
import com.withus.withus.global.security.UserDetailsImpl;
import com.withus.withus.member.entity.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {
    private final ClubRepository clubRepository;

    @Override
    public ClubResponseDto createClub(ClubRequestDto clubRequestDto, Member member) {
        LocalDateTime startTime = clubRequestDto.startTime();
        LocalDateTime endTime = clubRequestDto.endTime();
        Club club = new Club(clubRequestDto, member, startTime, endTime);
        Club savedClub = clubRepository.save(club);
        return new ClubResponseDto(savedClub);
    }

    @Override
    public ClubResponseDto getClub(Long clubId) {
        return new ClubResponseDto(findClubById(clubId));
    }
    private Club findClubById(Long clubId) {
        return clubRepository.findById(clubId).
                orElseThrow(() -> new BisException(ErrorCode.NOT_FOUND_CLUB));

    }


    @Override
    @Transactional
    public ClubResponseDto updateClub(Long clubId, ClubRequestDto clubrequestDto, UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        Club club = verifyMember(member,clubId);
        club.update(clubrequestDto);

        return new ClubResponseDto(club);
    }

    @Override
    public String deleteClub(Long clubId, UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        Club club = verifyMember(member, clubId);
        clubRepository.delete(club);
        return "Club deleted successfully";
    }

    private Club verifyMember(Member member, Long clubId) {
        Club club = findClubById(clubId);
        if(!club.getMember().getUsername().equals(member.getUsername())){
            throw new BisException(ErrorCode.NOT_FOUND_MEMBER);
        }
        return club;
    }
}