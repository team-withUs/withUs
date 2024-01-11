package com.withus.withus.comment.dto;

import com.withus.withus.global.timestamp.TimeStamp;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private Long id;
    private Long notice_id;
    private Long member_id;
    private int report;
    private boolean isActive;
    private TimeStamp created_at;
    private TimeStamp modified_at;
}
