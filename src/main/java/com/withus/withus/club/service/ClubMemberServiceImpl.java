package com.withus.withus.club.service;

import com.withus.withus.club.entity.Club;
import com.withus.withus.club.entity.ClubMember;
import com.withus.withus.club.entity.ClubMemberRole;
import com.withus.withus.club.repository.ClubMemberRepository;
import com.withus.withus.global.exception.BisException;
import com.withus.withus.global.exception.ErrorCode;
import com.withus.withus.member.entity.Member;
import jakarta.persistence.PreUpdate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubMemberServiceImpl implements ClubMemberService {

  private final ClubMemberRepository clubMemberRepository;


  public void createClubMember(ClubMember clubMember){
    clubMemberRepository.save(clubMember);
  }

  @Override
  public List<ClubMember> getInvitedUserByClub(Long clubId) {
    return clubMemberRepository.findByClubId(clubId);
  }


  public ClubMember findClubMemberByMemberIdAndClubId(Member member, Long clubId){
    return clubMemberRepository.findClubMemberByMemberIdAndClubId(member.getId(), clubId)
        .orElseThrow(() -> new BisException(ErrorCode.YOUR_NOT_COME_IN));
  }

  public Page<ClubMember> findAllByMemberId(Member member, Pageable pageable){
    return clubMemberRepository.findByMemberId(member.getId(), pageable);
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
