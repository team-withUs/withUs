package com.withus.withus.member.repository;

import com.withus.withus.member.entity.ReportMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportMemberRepository extends JpaRepository<ReportMember, Long> {

  boolean existsByReporterIdAndReportedId(Long id, Long memberId);

  int countByReportedId(Long memberId);
}
