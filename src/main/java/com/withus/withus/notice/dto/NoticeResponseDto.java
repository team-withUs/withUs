package com.withus.withus.notice.dto;

import com.withus.withus.notice.entity.Notice;
import java.time.LocalDateTime;
import lombok.Builder;


public record NoticeResponseDto(
  String title,
  String content

  )
{
  @Builder
  public NoticeResponseDto(String title,String content){
    this.title = title;
    this.content = content;
  }
  public static NoticeResponseDto createNoticeResponseDto(Notice notice){
    String title = notice.getTitle();
    String content = notice.getContent();

    return NoticeResponseDto.builder()
        .title(title)
        .content(content)
        .build();
  }
}
