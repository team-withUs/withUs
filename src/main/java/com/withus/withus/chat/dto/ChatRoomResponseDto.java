package com.withus.withus.chat.dto;

import com.withus.withus.member.entity.Member;
import lombok.Builder;

@Builder
public record ChatRoomResponseDto(
    Long roomId,
    String title,
    String senderImg

) {

  public static ChatRoomResponseDto createChatRoomResponseDto(
      Long roomId,
      String title,
      Member sender
  ) {
    return ChatRoomResponseDto.builder()
        .roomId(roomId)
        .title(title)
        .senderImg(sender.getImageURL())
        .build();
  }

}
