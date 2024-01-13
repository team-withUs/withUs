package com.withus.withus.comment.controller;

import com.withus.withus.comment.dto.CommentRequestDto;
import com.withus.withus.comment.dto.CommentResponseDto;
import com.withus.withus.comment.service.CommentService;
import com.withus.withus.global.annotation.AuthMember;
import com.withus.withus.global.response.CommonResponse;
import com.withus.withus.global.response.ResponseCode;
import com.withus.withus.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/notice/{noticeId}/comment")
    public ResponseEntity<CommonResponse<CommentResponseDto>> createComment(
            @PathVariable Long noticeId,
            @RequestBody CommentRequestDto commentRequestDto,
            @AuthMember Member member
    ) {
        CommentResponseDto commentResponseDto = commentService.createComment(noticeId, commentRequestDto, member);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_COMMENT_CREATE.getHttpStatus())
                .body(CommonResponse.of(ResponseCode.SUCCESS_COMMENT_CREATE, commentResponseDto));
    }
}
