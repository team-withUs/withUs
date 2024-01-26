package com.withus.withus.comment.dto;

import com.withus.withus.comment.entity.Comment;
import com.withus.withus.global.timestamp.TimeStamp;
import lombok.Builder;

import java.time.LocalDateTime;

public record CommentResponseDto (
        Long id,
        String content,
        String username  // 댓글의 작성자로 표기되는 것 = username(닉네임)
)
{
    @Builder
    public CommentResponseDto(Long id, String content, String username){
        this.id = id;
        this.content = content;
        this.username = username;
    }
    public static CommentResponseDto createCommentResponseDto(Comment savedcomment) {
        Long id = savedcomment.getId();
        String content = savedcomment.getContent();
        String username = savedcomment.getMember().getUsername();

        return CommentResponseDto.builder()
                .id(id)
                .content(content)
                .username(username)
                .build();
    }
}
