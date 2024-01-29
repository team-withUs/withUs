package com.withus.withus.domain.notice.repository;


import com.withus.withus.domain.notice.entity.ReportNotice;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<ReportNotice,Long> {
  boolean existsByNoticeIdAndMemberId(Long noticeId, Long memberId);

  int countByNoticeId(Long noticeId);

  List<ReportNotice> findAllByNoticeId(Long noticeId);

}
