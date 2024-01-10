package com.withus.withus.notice.repository;


import com.withus.withus.notice.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report,Long> {
  boolean existsByNoticeIdAndMemberId(Long noticeId, Long memberId);

}
