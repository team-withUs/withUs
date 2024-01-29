package com.withus.withus.domain.comment.repository;

import com.withus.withus.domain.comment.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIsActiveAndId(Boolean isActive, Long commentId);


    List<Comment> findAllByIsActiveAndNoticeId(boolean IsActive,Long noticeId, Pageable pageable);

    Integer countByIsActiveAndNoticeId(boolean isActive, Long noticeId);



    //삭제 스캐줄러용
    List<Comment> findAllByIsActive(boolean isActive);

    List<Comment> findAllByNoticeId(Long noticeId);
    List<Comment> findAllByMemberId(Long memberId);
}
