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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommonResponse<CommentResponseDto>> createComment(
            @RequestBody CommentRequestDto commentRequestDto, @AuthMember Member member){
        CommentResponseDto responseDto = commentService.createComment(commentRequestDto, member);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_COMMENT_CREATE.getHttpStatus())
                .body(CommonResponse.of(ResponseCode.SUCCESS_COMMENT_CREATE,responseDto));
    }
}
