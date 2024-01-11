package com.withus.withus.comment.service;

import com.withus.withus.comment.dto.CommentRequestDto;
import com.withus.withus.comment.dto.CommentResponseDto;
import com.withus.withus.member.entity.Member;

public interface CommentService {
    CommentResponseDto createComment(CommentRequestDto commentRequestDto, Member member);

}
