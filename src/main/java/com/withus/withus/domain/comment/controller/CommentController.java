package com.withus.withus.domain.comment.controller;

import com.withus.withus.domain.comment.dto.CommentDeleteRequestDto;
import com.withus.withus.domain.comment.dto.CommentRequestDto;
import com.withus.withus.domain.comment.dto.CommentResponseDto;
import com.withus.withus.domain.comment.dto.ReportRequestDto;
import com.withus.withus.domain.comment.service.CommentService;
import com.withus.withus.domain.notice.dto.PageableDto;
import com.withus.withus.global.annotation.AuthMember;
import com.withus.withus.global.response.CommonResponse;
import com.withus.withus.global.response.ResponseCode;
import com.withus.withus.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice/{noticeId}")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity<CommonResponse<CommentResponseDto>> createReportComment(
            @PathVariable("noticeId") Long noticeId,
            @RequestBody CommentRequestDto commentRequestDto,
            @AuthMember Member member
    ) {
        CommentResponseDto commentResponseDto = commentService.createComment(noticeId, commentRequestDto, member);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_COMMENT_CREATE.getHttpStatus())
                .body(CommonResponse.of(ResponseCode.SUCCESS_COMMENT_CREATE, commentResponseDto));
    }

    @PatchMapping("/comment/{commentId}")
    public ResponseEntity<CommonResponse<CommentResponseDto>> updateComment(
            @PathVariable("noticeId") Long noticeId,
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentRequestDto commentRequestDto,
            @AuthMember Member member
    ) {
        CommentResponseDto commentResponseDto = commentService.updateComment(noticeId, commentId, commentRequestDto, member);

        return ResponseEntity
            .status(ResponseCode.SUCCESS_COMMENT_UPDATE.getHttpStatus())
            .body(CommonResponse.of(ResponseCode.SUCCESS_COMMENT_UPDATE, commentResponseDto));
    }

    @GetMapping("/comment")
    public ResponseEntity<CommonResponse<Page<CommentResponseDto>>> getsComment(
            @PathVariable("noticeId") Long noticeId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "4") int size,
            @RequestParam(value = "sortBy", defaultValue = "CreatedAt") String sortBy
    ){
        PageableDto pageableDto = new PageableDto(page, size, sortBy);

        return ResponseEntity
            .status(ResponseCode.SUCCESS_COMMENT_GET.getHttpStatus())
            .body(CommonResponse.of(ResponseCode.SUCCESS_COMMENT_GET,
                        commentService.getComment(noticeId, pageableDto)));
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<CommonResponse<String>> deleteComment(
            @PathVariable("noticeId") Long noticeId,
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentDeleteRequestDto commentDeleteRequestDto,
            @AuthMember Member member
    ) {
        commentService.deleteComment(noticeId, commentId, member, commentDeleteRequestDto);

        return ResponseEntity
            .status(ResponseCode.SUCCESS_COMMENT_DELETE.getHttpStatus())
            .body(CommonResponse.of(ResponseCode.SUCCESS_COMMENT_DELETE,""));
    }

    @PostMapping("/comment/{commentId}/report")
    public ResponseEntity<CommonResponse<String>> createReportComment(
            @PathVariable("noticeId") Long noticeId,
            @PathVariable("commentId") Long commentId,
            @RequestBody ReportRequestDto requestDto,
            @AuthMember Member member
    ){
        commentService.createReportComment(commentId, requestDto, member);

        return ResponseEntity
            .status(ResponseCode.SUCCESS_COMMENT_REPORT.getHttpStatus())
            .body(CommonResponse.of(ResponseCode.SUCCESS_COMMENT_REPORT,""));
    }


}
