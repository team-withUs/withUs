package com.withus.withus.chat.dto;

import com.withus.withus.member.entity.Member;
import java.time.LocalDateTime;
import lombok.Builder;
@Builder
public record ChatMessageResponseDto(

    String senderName,
    String content,
    LocalDateTime sendTime
) {

  public static ChatMessageResponseDto createChatMessageResponseDto(
      Member sender,
      String content,
      LocalDateTime sendTime
  ) {

    return ChatMessageResponseDto.builder()
        .senderName(sender.getUsername())
        .content(content)
        .sendTime(sendTime)
        .build();
  }

}
