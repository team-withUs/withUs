package com.withus.withus.chat.dto;

import com.withus.withus.member.entity.Member;
import lombok.Builder;

@Builder
public record ChatRoomResponseDto(
    Long roomId,
    String sender,
    String receiver

) {
//  @Builder
//  public ChatRoomResponseDto(Long roomId, String sender, String receiver) {
//    this.roomId = roomId;
//    this.sender = sender;
//    this.receiver =receiver;
//  }

  public static ChatRoomResponseDto createChatRoomResponseDto(
      Long roomId,
      Member sender,
      Member receiver
  ) {
    return ChatRoomResponseDto.builder()
        .roomId(roomId)
        .sender(sender.getUsername())
        .receiver(receiver.getUsername())
        .build();
  }

}
