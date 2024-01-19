package com.withus.withus.member.dto;

import com.withus.withus.global.s3.S3Util;
import com.withus.withus.member.entity.Member;

import java.util.List;

import lombok.Builder;

@Builder
public record MemberResponseDto(
        Long id,
        String username,
        String email,
        String introduction,
        String imageURL
) {

    public static MemberResponseDto createMemberResponseDto(Member member) {
        String username = member.getUsername();
        String email = member.getEmail();
        String introduction = member.getIntroduction();
        String imageURL = member.getImageURL();

        return MemberResponseDto.builder()
                .username(username)
                .email(email)
                .introduction(introduction)
                .imageURL(imageURL)
                .build();
    }

    public static MemberResponseDto searchEmail(Member member) {
        String email = member.getEmail();
        Long id = member.getId();
        return MemberResponseDto.builder()
                .id(id)
                .email(email)
                .build();
    }
}
