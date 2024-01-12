package com.withus.withus.chat.dto;

import com.withus.withus.global.response.PageInfo;
import java.util.List;
import lombok.Builder;

@Builder
public record ChatMessagePageListResponseDto(
    List<ChatMessageResponseDto> chatMessageResponseDtos,
    PageInfo pageInfo
) {

//  @Builder
//  public ChatMessagePageListResponseDto(
//      List<ChatMessageResponseDto> chatMessageResponseDtos,
//      PageInfo pageInfo
//  ) {
//    this.chatMessageResponseDtos = chatMessageResponseDtos;
//    this.pageInfo = pageInfo;
//  }

  public static ChatMessagePageListResponseDto createChatMessagePageListResponseDto(
      List<ChatMessageResponseDto> chatMessageResponseDtos,
      PageInfo pageInfo
  ) {
    return ChatMessagePageListResponseDto.builder()
        .chatMessageResponseDtos(chatMessageResponseDtos)
        .pageInfo(pageInfo)
        .build();
  }
}

