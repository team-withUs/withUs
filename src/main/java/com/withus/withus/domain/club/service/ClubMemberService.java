package com.withus.withus.domain.club.service;

import com.withus.withus.domain.club.entity.ClubMember;
import com.withus.withus.domain.member.entity.Member;

import java.util.List;

public interface ClubMemberService {

    void createClubMember(ClubMember clubMember);

    List<ClubMember> getInvitedUserByClub(Long clubId);

    boolean hasHostRole(Member member, Long clubId);

    boolean isAuthorOrHost(Member member, Long clubId);

    Integer getClubMemberCount(Long clubId);

    String leaveClub(Long clubId, Member member);
}
