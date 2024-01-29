package com.withus.withus.domain.comment.repository;

import com.withus.withus.domain.comment.entity.ReportComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReportRepository extends JpaRepository<ReportComment, Long> {
    boolean existsByCommentIdAndMemberId(Long commentId, Long memberId);

    int countByCommentId(Long commentId);
}
