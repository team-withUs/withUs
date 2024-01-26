package com.withus.withus.member.dto;

import lombok.Builder;
@Builder
public record ChatMemberResponseDto(
    Long id,
    String username,
    String image
) {

  public static ChatMemberResponseDto createChatMemberResponseDto(Long id, String username, String image) {
    return ChatMemberResponseDto.builder()
        .id(id)
        .username(username)
        .image(image)
        .build();
  }

}
