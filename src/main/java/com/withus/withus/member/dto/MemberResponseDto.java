package com.withus.withus.member.dto;

import com.withus.withus.member.entity.Member;
import java.util.List;
import lombok.Builder;

public record MemberResponseDto(
    String username,
    String email,
    String introduction,
    String image
) {


  @Builder
  public MemberResponseDto(String username, String email, String introduction, String image) {
    this.username = username;
    this.email = email;
    this.introduction = introduction;
    this.image = image;
  }

  public static MemberResponseDto buildMemberResponseDto(Member member){
    String username = member.getUsername();
    String email = member.getEmail();
    String introduction = member.getIntroduction();
    String image = member.getImage();

    return MemberResponseDto.builder()
        .username(username)
        .email(email)
        .introduction(introduction)
        .image(image)
        .build();
  }
}
