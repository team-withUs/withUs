package com.withus.withus.domain.comment.service;

import com.withus.withus.domain.comment.dto.CommentDeleteRequestDto;
import com.withus.withus.domain.comment.dto.CommentRequestDto;
import com.withus.withus.domain.comment.dto.CommentResponseDto;
import com.withus.withus.domain.comment.dto.ReportRequestDto;
import com.withus.withus.domain.notice.dto.PageableDto;
import com.withus.withus.domain.member.entity.Member;

import java.util.List;
import org.springframework.data.domain.Page;

public interface CommentService {
    CommentResponseDto createComment(Long noticeId, CommentRequestDto commentRequestDto, Member member);

    CommentResponseDto updateComment(Long noticeId, Long commentId, CommentRequestDto commentRequestDto, Member member);

    Page<CommentResponseDto> getComment(Long noticeId, PageableDto pageableDto);

    void deleteComment(Long noticeId, Long commentId, Member member, CommentDeleteRequestDto commentDeleteRequestDto);

    void createReportComment(Long commentId, ReportRequestDto RequestDto, Member member);

    Integer count(Long noticeId);
}
