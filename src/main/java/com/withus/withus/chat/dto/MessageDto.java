package com.withus.withus.chat.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessageDto {

  @NotNull
  private Long senderId;
  @NotNull
  private String senderName;
  private String content;

  @Builder
  private MessageDto(Long senderId, String senderName, String content) {
    this.senderId = senderId;
    this.senderName = senderName;
    this.content = content;
  }

  public static MessageDto createMessageDto(Long senderId, String senderName, String content) {
    return MessageDto.builder()
        .senderId(senderId)
        .senderName(senderName)
        .content(content)
        .build();
  }
  public void setContent(String content) {
    this.content = content;
  }

}
