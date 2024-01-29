package com.withus.withus.domain.comment.dto;

import com.withus.withus.domain.comment.entity.Comment;
import lombok.Builder;

@Builder
public record CommentResponseDto (
        Long id,
        String content,
        String username  // 댓글의 작성자로 표기되는 것 = username(닉네임)
) {
    public static CommentResponseDto createCommentResponseDto(Comment savedcomment) {

        return CommentResponseDto.builder()
                .id(savedcomment.getId())
                .content(savedcomment.getContent())
                .username(savedcomment.getMember().getUsername())
                .build();
    }
}
