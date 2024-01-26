package com.withus.withus.club.repository;

import com.withus.withus.club.entity.ClubMemberRole;

public interface ClubMemberRepositoryQuery{

  boolean existHost(ClubMemberRole clubMemberRole, Long memberId, Long clubId);

}
