package com.withus.withus.comment.repository;

import com.withus.withus.comment.entity.ReportComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<ReportComment, Long> {
    boolean existsByCommentIdAndMemberId(Long commentId, Long memberId);

    int countByCommentId(Long commentId);
}
