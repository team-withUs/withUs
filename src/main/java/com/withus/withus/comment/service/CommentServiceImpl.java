package com.withus.withus.comment.service;

import com.withus.withus.comment.dto.CommentRequestDto;
import com.withus.withus.comment.dto.CommentResponseDto;
import com.withus.withus.comment.entity.Comment;
import com.withus.withus.comment.repository.CommentRepository;
import com.withus.withus.member.entity.Member;
import com.withus.withus.notice.entity.Notice;
import com.withus.withus.notice.service.NoticeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    private NoticeServiceImpl noticeService;

    @Override
    public CommentResponseDto createComment(Long noticeId, CommentRequestDto commentRequestDto, Member member){
        Notice notice = noticeService.findByIsActiveAndNoticeId(noticeId);
        Comment savedComment = commentRepository.save(Comment.createComment(commentRequestDto, member, notice));
        return CommentResponseDto.createCommentResponseDto(savedComment);
    }
}
