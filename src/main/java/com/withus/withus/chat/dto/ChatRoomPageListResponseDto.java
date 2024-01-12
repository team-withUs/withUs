package com.withus.withus.chat.dto;

import com.withus.withus.global.response.PageInfo;
import java.util.List;
import lombok.Builder;

@Builder
public record ChatRoomPageListResponseDto(
    List<ChatRoomResponseDto> chatRoomResponseDtos,
    PageInfo pageInfo
) {
//  @Builder
//  public ChatRoomPageListResponseDto(
//      List<ChatRoomResponseDto> chatRoomResponseDtos,
//      PageInfo pageInfo
//  ) {
//    this.chatRoomResponseDtos = chatRoomResponseDtos;
//    this.pageInfo = pageInfo;
//  }

  public static ChatRoomPageListResponseDto createChatPageListResponseDto(
      List<ChatRoomResponseDto> chatRoomResponseDtos,
      PageInfo pageInfo
  ) {
    return ChatRoomPageListResponseDto.builder()
        .chatRoomResponseDtos(chatRoomResponseDtos)
        .pageInfo(pageInfo)
        .build();
  }

}
