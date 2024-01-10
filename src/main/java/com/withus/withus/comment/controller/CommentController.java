package com.withus.withus.comment.controller;

import com.withus.withus.comment.dto.CommentRequestDto;
import com.withus.withus.comment.dto.CommentResponseDto;
import com.withus.withus.global.response.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @PostMapping
    public ResponseEntity<CommonResponse<CommentResponseDto>> createComment(
            @RequestBody CommentRequestDto commentRequestDto){

        return null;
    }
}
