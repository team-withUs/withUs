package com.withus.withus.domain.member.dto;

import com.withus.withus.domain.member.entity.Member;

import lombok.Builder;

@Builder
public record MemberResponseDto(
        Long id,
        String username,
        String email,
        String introduction,
        String imageURL,
        String tag
) {
    public static MemberResponseDto createMemberResponseDto(Member member){
        String username = member.getUsername();
        String email = member.getEmail();
        String introduction = member.getIntroduction();
        String imageURL = member.getImageURL();
        String tag = member.getTag();
        return MemberResponseDto.builder()
                .username(username)
                .email(email)
                .introduction(introduction)
                .imageURL(imageURL)
                .tag(tag)
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
