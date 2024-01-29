package com.withus.withus.domain.member.repository;

import com.withus.withus.domain.member.entity.ReportMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportMemberRepository extends JpaRepository<ReportMember, Long> {

  ReportMember findByReportedIdAndReporterId(Long reporterId, Long reportedId);

  boolean existsByReporterIdAndReportedId(Long reporterId, Long reportedId);

  int countByReportedId(Long memberId);
}
