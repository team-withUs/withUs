package com.withus.withus.chat.dto;

import java.time.LocalDateTime;
import lombok.Builder;
@Builder
public record ChatMessageResponseDto(
    Long messageId,
    String sender,
    String content,
    LocalDateTime sendTime
) {

//  @Builder
//  public ChatMessageResponseDto(Long messageId, String sender, String content, LocalDateTime sendTime) {
//    this.messageId = messageId;
//    this.sender = sender;
//    this.content = content ;
//    this.sendTime = sendTime;
//  }

  public static ChatMessageResponseDto createChatMessageResponseDto(
      Long messageId,
      String sender,
      String content,
      LocalDateTime sendTime
  ) {

    return ChatMessageResponseDto.builder()
        .messageId(messageId)
        .sender(sender)
        .content(content)
        .sendTime(sendTime)
        .build();
  }

}
