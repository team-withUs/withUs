package com.withus.withus.member.dto;

import lombok.Builder;

public record ChatMemberResponseDto(
    Long id,
    String username,
    String image
) {

  @Builder
  public ChatMemberResponseDto(Long id, String username, String image) {
    this.id = id;
    this.username = username;
    this.image = image;
  }

  public static ChatMemberResponseDto createChatMemberResponseDto(Long id, String username, String image) {
    return ChatMemberResponseDto.builder()
        .id(id)
        .username(username)
        .image(image)
        .build();
  }

}
