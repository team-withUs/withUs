package com.withus.withus.club.dto;

import com.withus.withus.club.entity.ClubMember;
import com.withus.withus.club.entity.ClubMemberRole;
import com.withus.withus.member.entity.Member;
import lombok.Builder;

@Builder
public record InviteMemberResponseDto(
    Long id,
    ClubMemberRole clubMemberRole,
    String username,
    String imageURL

) {

  public static InviteMemberResponseDto createInviteMemberResponseDto(ClubMember clubMember) {
    Long id = clubMember.getMember().getId();
    ClubMemberRole clubMemberRole1 = clubMember.getClubMemberRole();
    String username = clubMember.getMember().getUsername();
    String imageURL = clubMember.getMember().getImageURL();
    return InviteMemberResponseDto.builder()
        .id(id)
        .clubMemberRole(clubMemberRole1)
        .username(username)
        .imageURL(imageURL)
        .build();
  }
}
