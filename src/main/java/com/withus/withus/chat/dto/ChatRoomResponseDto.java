package com.withus.withus.chat.dto;

import com.withus.withus.member.entity.Member;
import lombok.Builder;

@Builder
public record ChatRoomResponseDto(
    Long roomId,
    String title,
    String sender,
    String receiver

) {

  public static ChatRoomResponseDto createChatRoomResponseDto(
      Long roomId,
      String title,
      Member sender,
      Member receiver
  ) {
    return ChatRoomResponseDto.builder()
        .roomId(roomId)
        .title(title)
        .sender(sender.getUsername())
        .receiver(receiver.getUsername())
        .build();
  }

}
