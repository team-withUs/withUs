package com.withus.withus.club.service;

import com.withus.withus.club.entity.ClubMember;
import com.withus.withus.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClubMemberService {

    void createClubMember(ClubMember clubMember);
}
