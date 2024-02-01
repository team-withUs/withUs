package com.withus.withus.domain.club.repository;

import com.withus.withus.domain.club.entity.ClubMember;
import com.withus.withus.domain.club.entity.ClubMemberRole;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long>, ClubMemberRepositoryQuery {

    Optional<ClubMember> findClubMemberByMemberIdAndClubId(Long memberId, Long clubId);

    Page<ClubMember> findByMemberIdAndClub_IsActive(Long member_id, Pageable pageable, boolean isActive);

    Page<ClubMember> findByMemberIdAndClubMemberRoleAndClub_IsActive(
        Long memberId,
        ClubMemberRole role,
        boolean isActive,
        Pageable pageable
    );

    boolean existsByMemberIdAndClubId(Long memberId, Long clubId);

    List<ClubMember> findByClubIdAndMember_IsActive(Long clubId, boolean memberIsActive);

    //  추가
    Integer countByClubIdAndMember_IsActive(Long clubId, boolean memberIsActive);

    //삭제 스케줄러용
    List<ClubMember> findAllByClubId(Long clubId);

    List<ClubMember> findAllByMemberId(Long memberId);

    List<ClubMember> findByMemberIdAndClubMemberRole(Long memberId, ClubMemberRole role);
}
