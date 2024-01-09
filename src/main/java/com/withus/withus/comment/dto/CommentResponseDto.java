package com.withus.withus.comment.dto;

import com.withus.withus.global.timestamp.Timestamp;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private Long id;
    private Long notice_id;
    private Long menmber_id;
    private int report;
    private boolean isActive;
    private Timestamp created_at;
    private Timestamp modified_at;
}
