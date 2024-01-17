package com.withus.withus.comment.dto;

import com.withus.withus.comment.entity.Comment;
import com.withus.withus.global.timestamp.TimeStamp;
import lombok.Builder;

import java.time.LocalDateTime;

public record CommentResponseDto (
        String content,
        String username,  // 댓글의 작성자로 표기되는 것 = username(닉네임)
        LocalDateTime created_at // 작성일을 생성일로
)
{
    @Builder
    public CommentResponseDto(String content, String username, LocalDateTime created_at){
        this.content = content;
        this.username = username;
        this.created_at = created_at;
    }
    public static CommentResponseDto createCommentResponseDto(Comment savedcomment) {
        String content = savedcomment.getContent();
        String username = savedcomment.getMember().getUsername();
        LocalDateTime created_at = savedcomment.getCreatedAt();

        return CommentResponseDto.builder()
                .content(content)
                .username(username)
                .created_at(LocalDateTime.now())
                .build();
    }
}
