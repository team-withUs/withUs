package com.withus.withus.comment.service;

import com.withus.withus.club.entity.Club;
import com.withus.withus.comment.dto.CommentRequestDto;
import com.withus.withus.comment.dto.CommentResponseDto;
import com.withus.withus.comment.entity.Comment;
import com.withus.withus.comment.repository.CommentRepository;
import com.withus.withus.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;

    @Override
    public CommentResponseDto createComment(CommentRequestDto commentRequestDto, Member member){
        LocalDateTime startTime = commentRequestDto.startTime();
        LocalDateTime endTime = commentRequestDto.endTime();
        Comment comment = Comment.createComment(commentRequestDto, member, startTime, endTime);
        Comment savedComment = commentRepository.save(comment);
        return CommentResponseDto.createCommentResponseDto(savedComment);
    }
}
