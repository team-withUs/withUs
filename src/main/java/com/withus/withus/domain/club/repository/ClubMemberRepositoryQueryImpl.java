package com.withus.withus.domain.club.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.withus.withus.domain.club.entity.ClubMemberRole;
import lombok.RequiredArgsConstructor;
import static com.withus.withus.domain.club.entity.QClubMember.clubMember;

@RequiredArgsConstructor
public class ClubMemberRepositoryQueryImpl implements ClubMemberRepositoryQuery {
  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public boolean existHost(ClubMemberRole clubMemberRole, Long memberId, Long clubId) {
    Integer existHost = jpaQueryFactory
        .selectOne()
        .from(clubMember)
        .where(clubMember.clubMemberRole.eq(clubMemberRole),
            clubMember.member.id.eq(memberId),
            clubMember.club.id.eq(clubId))
        .fetchFirst();
    return existHost != null;
  }
}
