package com.withus.withus.member.repository;

import com.withus.withus.member.entity.ReportMember;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportMemberRepository extends JpaRepository<ReportMember, Long> {

  ReportMember findByReportedIdAndReporterId(Long reporterId, Long reportedId);

  boolean existsByReporterIdAndReportedId(Long reporterId, Long reportedId);

  int countByReportedId(Long memberId);
}
