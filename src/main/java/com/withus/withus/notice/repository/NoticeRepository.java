package com.withus.withus.notice.repository;

import com.withus.withus.notice.entity.Notice;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice,Long> {
  Optional<Notice> findByIsActiveAndId(Boolean isActive,Long noticeId);
}
