package com.withus.withus.domain.chat.dto;

import com.withus.withus.domain.member.entity.Member;
import lombok.Builder;

@Builder
public record ChatRoomResponseDto(
    Long roomId,
    String title,
    Long senderId,
    String senderImg,
    Long receiverId,
    String receiverImg

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
        .senderImg(sender.getImageURL())
        .senderId(sender.getId())
        .receiverId(receiver.getId())
        .receiverImg(receiver.getImageURL())
        .build();
  }

}
