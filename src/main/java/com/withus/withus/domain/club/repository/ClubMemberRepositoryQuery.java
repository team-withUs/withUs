package com.withus.withus.domain.club.repository;

import com.withus.withus.domain.club.entity.ClubMemberRole;

public interface ClubMemberRepositoryQuery{

  boolean existHost(ClubMemberRole clubMemberRole, Long memberId, Long clubId);

}
