package com.withus.withus.member.dto;

import com.withus.withus.global.s3.S3Util;
import com.withus.withus.member.entity.Member;
import java.util.List;
import lombok.Builder;

public record MemberResponseDto(
    String username,
    String email,
    String introduction,
    String imageURL
) {


  @Builder
  public MemberResponseDto(String username, String email, String introduction, String imageURL) {
    this.username = username;
    this.email = email;
    this.introduction = introduction;
    this.imageURL = imageURL;
  }

  public static MemberResponseDto createMemberResponseDto(Member member, String imageURL){
    String username = member.getUsername();
    String email = member.getEmail();
    String introduction = member.getIntroduction();

    return MemberResponseDto.builder()
        .username(username)
        .email(email)
        .introduction(introduction)
        .imageURL(imageURL)
        .build();
  }
}
