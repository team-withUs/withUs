package com.withus.withus.comment.controller;

import com.withus.withus.comment.dto.CommentRequestDto;
import com.withus.withus.comment.dto.CommentResponseDto;
import com.withus.withus.comment.dto.ReportRequestDto;
import com.withus.withus.comment.service.CommentService;
import com.withus.withus.global.annotation.AuthMember;
import com.withus.withus.global.response.CommonResponse;
import com.withus.withus.global.response.ResponseCode;
import com.withus.withus.member.entity.Member;
import com.withus.withus.notice.dto.PageableDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/notice/{noticeId}/comment")
    public ResponseEntity<CommonResponse<CommentResponseDto>> createReportComment(
            @PathVariable Long noticeId,
            @RequestBody CommentRequestDto commentRequestDto,
            @AuthMember Member member
    ) {
        CommentResponseDto commentResponseDto = commentService.createComment(noticeId, commentRequestDto, member);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_COMMENT_CREATE.getHttpStatus())
                .body(CommonResponse.of(ResponseCode.SUCCESS_COMMENT_CREATE, commentResponseDto));
    }

    @PatchMapping("/notice/{noticeId}/comment/{commentId}")
    public ResponseEntity<CommonResponse<CommentResponseDto>> updateComment(
            @PathVariable Long noticeId,
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto commentRequestDto,
            @AuthMember Member member
    ) {
        CommentResponseDto commentResponseDto = commentService.updateComment(noticeId, commentId, commentRequestDto, member);
        return ResponseEntity.status(ResponseCode.SUCCESS_COMMENT_UPDATE.getHttpStatus())
                .body(CommonResponse.of(ResponseCode.SUCCESS_COMMENT_UPDATE, commentResponseDto));
    }

    @GetMapping("/noice/{noticeId}/comment")
    public ResponseEntity<CommonResponse<List<CommentResponseDto>>> getsComment(
            @PathVariable Long noticeId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "createdDate") String sortBy
    ){
        PageableDto pageableDto = new PageableDto(page, size, sortBy);
        return ResponseEntity.status(ResponseCode.SUCCESS_COMMENT_GET.getHttpStatus())
                .body(CommonResponse.of(ResponseCode.SUCCESS_COMMENT_GET,
                        commentService.getComment(noticeId, pageableDto)));
    }

    @DeleteMapping("/notice/{noticeId}/comment/{commentId}")
    public ResponseEntity<CommonResponse<String>> deleteComment(
            @PathVariable Long noticeId,
            @PathVariable Long commentId,
            @AuthMember Member member
    ) {
        commentService.deleteComment(noticeId, commentId, member);
        return ResponseEntity.status(ResponseCode.SUCCESS_COMMENT_DELETE.getHttpStatus())
                .body(CommonResponse.of(ResponseCode.SUCCESS_COMMENT_DELETE,""));
    }

    @PostMapping("/comment/{commentId}/report")
    public ResponseEntity<CommonResponse<String>> createReportComment(
            @PathVariable Long commentId,
            @RequestBody ReportRequestDto requestDto,
            @AuthMember Member member
    ){
        commentService.createReportComment(commentId, requestDto, member);
        return ResponseEntity.status(ResponseCode.SUCCESS_COMMENT_REPORT.getHttpStatus())
                .body(CommonResponse.of(ResponseCode.SUCCESS_COMMENT_REPORT,""));
    }


}
