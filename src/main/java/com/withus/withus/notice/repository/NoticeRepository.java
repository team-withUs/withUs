package com.withus.withus.notice.repository;

import com.withus.withus.notice.dto.PageableDto;
import com.withus.withus.notice.entity.Notice;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice,Long> {
  Optional<Notice> findByIsActiveAndId(Boolean isActive,Long noticeId);

  List<Notice> findAllByIsActive(Boolean isActive,Pageable pageable);
}
