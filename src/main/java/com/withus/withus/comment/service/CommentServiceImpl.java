package com.withus.withus.comment.service;

import com.withus.withus.comment.dto.CommentRequestDto;
import com.withus.withus.comment.dto.CommentResponseDto;
import com.withus.withus.comment.entity.Comment;
import com.withus.withus.comment.repository.CommentRepository;
import com.withus.withus.global.exception.BisException;
import com.withus.withus.global.exception.ErrorCode;
import com.withus.withus.global.response.CommonResponse;
import com.withus.withus.member.entity.Member;
import com.withus.withus.notice.dto.PageableDto;
import com.withus.withus.notice.entity.Notice;
import com.withus.withus.notice.service.NoticeServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<CommentResponseDto> getComment(Long noticeId, PageableDto pageableDto){
        if(!existsByNoticeId(noticeId)){
            throw new BisException(ErrorCode.NOT_FOUND_NOTICE);
        }
        List<Comment> commentList = commentRepository
                .findAllByIsActive(true,PageableDto
                        .getsPageableDto(
                                pageableDto.page(),
                                pageableDto.size(),
                                pageableDto.sortBy()
                        ).toPageable()
                );
        List<CommentResponseDto> responseDtoList = new ArrayList<>();
        for(Comment comment : commentList){
            responseDtoList.add(CommentResponseDto.createCommentResponseDto(comment));
        }
        return responseDtoList;
    }

    @Transactional
    @Override
    public void deleteComment(Long noticeId, Long commentId, Member member){
        if(!existsByNoticeId(noticeId)){
            throw new BisException(ErrorCode.NOT_FOUND_NOTICE);
        }
        // 튜터님 및 팀원과 상의하기 -> 댓글 작성자만 삭제하려면 어떻게..?

        Comment comment = findByIsActiveAndCommentId(commentId);
        comment.inActive();
    }

    private boolean existsByNoticeId(Long noticeId) {
        return noticeService.existByIsActiveAndClubId(noticeId);
    }

    private Comment findByIsActiveAndCommentId(Long commentId) {
        Comment comment = commentRepository.findByIsActiveAndId(true, commentId)
                .orElseThrow(()->
                        new BisException(ErrorCode.NOT_FOUND_COMMENT)
                );
        return comment;
    }
}
