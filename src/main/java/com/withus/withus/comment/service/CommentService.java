package com.withus.withus.comment.service;

import com.withus.withus.comment.dto.CommentRequestDto;
import com.withus.withus.comment.dto.CommentResponseDto;
import com.withus.withus.comment.dto.ReportRequestDto;
import com.withus.withus.member.entity.Member;
import com.withus.withus.notice.dto.PageableDto;

import java.util.List;

public interface CommentService {
    CommentResponseDto createComment(Long noticeId, CommentRequestDto commentRequestDto, Member member);

    CommentResponseDto updateComment(Long noticeId, Long commentId, CommentRequestDto commentRequestDto, Member member);

    List<CommentResponseDto> getComment(Long noticId, PageableDto pageableDto);

    void deleteComment(Long noticeId, Long commentId, Member member);

    void createReportComment(Long commentId, ReportRequestDto RequestDto, Member member);
}
