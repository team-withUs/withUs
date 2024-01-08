package com.withus.withus.notice.dto;

import com.withus.withus.notice.entity.Notice;
import java.time.LocalDateTime;




public record NoticeResponseDto(
  String title,
  String content,
  LocalDateTime createdAt,
  LocalDateTime modifiedAt
  )
{

  public NoticeResponseDto(Notice notice){
    this(notice.getTitle(), notice.getContent(),
        notice.getCreatedAt(),notice.getModifiedAt());
  }
}
