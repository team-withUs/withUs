package com.withus.withus.comment.repository;

import com.withus.withus.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIsActiveAndId(Boolean isActive, Long commentId);
}
