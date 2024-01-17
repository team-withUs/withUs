package com.withus.withus.chat.dto;

import com.withus.withus.member.entity.Member;
import java.time.LocalDateTime;
import lombok.Builder;
@Builder
public record ChatMessageResponseDto(
    Long messageId,
    Long senderId,
    String senderName,
    String content,
    LocalDateTime sendTime
) {

  public static ChatMessageResponseDto createChatMessageResponseDto(
      Long messageId,
      Member sender,
      String content,
      LocalDateTime sendTime
  ) {

    return ChatMessageResponseDto.builder()
        .messageId(messageId)
        .senderId(sender.getId())
        .senderName(sender.getUsername())
        .content(content)
        .sendTime(sendTime)
        .build();
  }

}
