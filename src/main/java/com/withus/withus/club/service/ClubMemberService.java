package com.withus.withus.club.service;

import com.withus.withus.club.entity.ClubMember;
import com.withus.withus.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClubMemberService {

    void createClubMember(ClubMember clubMember);

    List<ClubMember> getInvitedUserByClub(Long clubId);

    boolean hasHostRole(Member member, Long clubId);

    boolean isAuthorOrHost(Member member, Long clubId);

    Integer getClubMemberCount(Long clubId);

    String leaveClub(Long clubId, Member member);
}
