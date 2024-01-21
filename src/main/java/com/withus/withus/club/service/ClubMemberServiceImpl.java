package com.withus.withus.club.service;

import com.withus.withus.club.entity.ClubMember;
import com.withus.withus.club.entity.ClubMemberRole;
import com.withus.withus.club.repository.ClubMemberRepository;
import com.withus.withus.global.exception.BisException;
import com.withus.withus.global.exception.ErrorCode;
import com.withus.withus.member.entity.Member;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubMemberServiceImpl implements ClubMemberService {

    private final ClubMemberRepository clubMemberRepository;


    public void createClubMember(ClubMember clubMember) {
        clubMemberRepository.save(clubMember);
    }

    @Override
    public List<ClubMember> getInvitedUserByClub(Long clubId) {
        return clubMemberRepository.findByClubId(clubId);
    }

    //추가
    @Override
    public boolean hasHostRole(Member member, Long clubId) {
        ClubMember clubMember = clubMemberRepository.findClubMemberByMemberIdAndClubId(member.getId(), clubId)
                .orElseThrow(() -> new BisException(ErrorCode.YOUR_NOT_COME_IN));

        return clubMember.getClubMemberRole() == ClubMemberRole.HOST;
    }

    @Override
    public boolean isAuthorOrHost(Member member, Long clubId) {
        ClubMember clubMember = clubMemberRepository.findClubMemberByMemberIdAndClubId(member.getId(), clubId)
                .orElseThrow(() -> new BisException(ErrorCode.YOUR_NOT_COME_IN));

        // Check if the member is the author or has the host role
        return clubMember.getClub().getAuthor().equals(member) || clubMember.getClubMemberRole() == ClubMemberRole.HOST;
    }


    public ClubMember findClubMemberByMemberIdAndClubId(Member member, Long clubId) {
        return clubMemberRepository.findClubMemberByMemberIdAndClubId(member.getId(), clubId)
                .orElseThrow(() -> new BisException(ErrorCode.YOUR_NOT_COME_IN));
    }

    public Page<ClubMember> findAllByMemberId(Member member, Pageable pageable) {
        return clubMemberRepository.findByMemberId(member.getId(), pageable);
    }

    public boolean existsClubMemberByMemberIdAndClubId(Long memberId, Long clubId) {
        return clubMemberRepository.existsByMemberIdAndClubId(memberId, clubId);
    }

    public boolean isHostMember(Long memberId, Long clubId) {
        Optional<ClubMember> member = clubMemberRepository.findClubMemberByMemberIdAndClubId(memberId, clubId);
        if (member.isPresent()) {
            if (ClubMemberRole.HOST == member.get().getClubMemberRole()) {
                return true;
            }
        }
        return false;
    }


  public boolean existsClubMemberByMemberIdAndClubId(Long memberId, Long clubId){
    return clubMemberRepository.existsByMemberIdAndClubId(memberId, clubId);
  }

  public Page<ClubMember> findByMemberIdAndClubMemberRole(
      Long memberId,
      ClubMemberRole role,
      Pageable pageable
  ) {
    return clubMemberRepository.findByMemberIdAndClubMemberRole(memberId, role, pageable);
  }
}

