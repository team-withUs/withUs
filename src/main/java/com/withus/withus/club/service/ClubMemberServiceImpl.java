package com.withus.withus.club.service;

import com.withus.withus.club.entity.Club;
import com.withus.withus.club.entity.ClubMember;
import com.withus.withus.club.repository.ClubMemberRepository;
import com.withus.withus.member.entity.Member;
import jakarta.persistence.PreUpdate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubMemberServiceImpl {

  private final ClubMemberRepository clubMemberRepository;

  public void createClubMember(ClubMember clubMember){
    clubMemberRepository.save(clubMember);
  }

  public ClubMember findClubMemberByMemberIdAndClubId(Member member, Long clubId){
    return clubMemberRepository.findClubMemberByMemberIdAndClubId(member.getId(), clubId);
  }

  public Page<ClubMember> findAllByMemberId(Member member, Pageable pageable){
    return clubMemberRepository.findByMemberId(member.getId(), pageable);
  }
}
