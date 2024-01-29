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

    public static MemberResponseDto createMemberResponseDto(Member member) {

        return MemberResponseDto.builder()
                .username(member.getUsername())
                .email(member.getEmail())
                .introduction(member.getIntroduction())
                .imageURL(member.getImageURL())
                .tag(member.getTag())
                .build();
    }


    public static MemberResponseDto searchEmail(Member member) {

        return MemberResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .build();
    }

}
