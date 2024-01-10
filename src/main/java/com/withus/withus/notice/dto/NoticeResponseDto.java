package com.withus.withus.notice.dto;

import com.withus.withus.notice.entity.Notice;
import java.time.LocalDateTime;
import lombok.Builder;


public record NoticeResponseDto(
  String title,
  String content,
  int count

  )
{
  @Builder
  public NoticeResponseDto(String title,String content,int count){
    this.title = title;
    this.content = content;
    this.count = count;
  }
  public static NoticeResponseDto createNoticeResponseDto(Notice notice){
    String title = notice.getTitle();
    String content = notice.getContent();
    int count = notice.getReportList().size();

    return NoticeResponseDto.builder()
        .title(title)
        .content(content)
        .count(count)
        .build();
  }
}
