package com.withus.withus.comment.service;

import com.withus.withus.comment.dto.CommentRequestDto;
import com.withus.withus.comment.dto.CommentResponseDto;
import com.withus.withus.comment.entity.Comment;
import com.withus.withus.comment.repository.CommentRepository;
import com.withus.withus.global.exception.BisException;
import com.withus.withus.global.exception.ErrorCode;
import com.withus.withus.member.entity.Member;
import com.withus.withus.notice.entity.Notice;
import com.withus.withus.notice.service.NoticeServiceImpl;
import jakarta.transaction.Transactional;
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

    @Transactional
    @Override
    public  CommentResponseDto updateComment(Long noticeId, Long commentId, CommentRequestDto commentRequestDto, Member member){
        Comment comment = findByIsActiveAndCommentId(commentId);
        comment.update(commentRequestDto);
        return CommentResponseDto.createCommentResponseDto(comment);
    }

    private Comment findByIsActiveAndCommentId(Long commentId) {
        Comment comment = commentRepository.findByIsActiveAndId(true, commentId)
                .orElseThrow(()->
                        new BisException(ErrorCode.NOT_FOUND_COMMENT)
                );
        return comment;
    }
}
