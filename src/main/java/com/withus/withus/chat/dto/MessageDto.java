package com.withus.withus.chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessageDto implements Serializable {

  @NotNull
  private Long senderId;
  @NotBlank
  private String content;

  @Builder
  public MessageDto(Long senderId, String content) {
    this.senderId = senderId;
    this.content = content;
  }

  public MessageDto createMessageDto(Long senderId, String content) {
    return MessageDto.builder()
        .senderId(senderId)
        .content(content)
        .build();
  }
  public void setContent(String content) {
    this.content = content;
  }

}
