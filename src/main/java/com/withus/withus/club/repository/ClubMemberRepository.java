package com.withus.withus.club.repository;

import com.withus.withus.club.entity.ClubMember;
import com.withus.withus.member.entity.Member;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {

  ClubMember findClubMemberByMemberIdAndClubId(Long memberId, Long clubId );

  Page<ClubMember> findByMemberId(Long member_id, Pageable pageable);
}
