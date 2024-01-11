package com.withus.withus.club.repository;

import com.withus.withus.club.entity.ReportClub;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportClubRepository extends JpaRepository<ReportClub, Long> {

    int countByClubId(Long clubId);

    boolean existsByClubIdAndMemberId(Long clubId, Long memberId);
}
