package com.withus.withus.domain.notice.repository;



import com.withus.withus.domain.notice.entity.Notice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice,Long> {

  Optional<Notice> findByIsActiveAndId(Boolean isActive,Long noticeId);

  List<Notice> findAllByIsActiveAndClubId(Boolean isActive,Long clubId, Pageable pageable);

  boolean existsByIsActiveAndId(boolean isActive, Long noticeId);

  Integer countByClubIdAndIsActive(Long clubId, boolean isActive);


  //삭제 스캐줄러용
  List<Notice> findAllByIsActive(boolean isActive);

  List<Notice> findAllByClubId(Long clubId);

  List<Notice> findAllByMemberId(Long memberId);

}
