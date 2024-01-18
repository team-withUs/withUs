package com.withus.withus.club.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.withus.withus.category.entity.ClubCategory;
import com.withus.withus.club.entity.Club;
import com.withus.withus.notice.dto.PageableDto;
import java.util.Currency;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import static com.withus.withus.club.entity.QClub.club;

@RequiredArgsConstructor
public class ClubRepositoryQueryImpl implements ClubRepositoryQuery {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<Club> search(String keyWord, boolean isActive, Pageable pageable, ClubCategory category) {
    List<Club> list;
    if(category.equals(ClubCategory.ALL)){
      list = jpaQueryFactory
          .select(club)
          .from(club)
          .where(club.isActive.eq(true),containsSearchTitle(keyWord))
          .orderBy(club.createdAt.desc())
          .offset(pageable.getOffset())
          .limit(pageable.getPageSize())
          .fetch();
    }
    else {
      list = jpaQueryFactory
          .select(club)
          .from(club)
          .where(club.isActive.eq(true),club.category.eq(category),containsSearchTitle(keyWord))
          .orderBy(club.createdAt.desc())
          .offset(pageable.getOffset())
          .limit(pageable.getPageSize())
          .fetch();
    }
    return list;
  }

  private BooleanExpression containsSearchTitle(String keyWord){
    return keyWord != null ? club.clubTitle.contains(keyWord) : null;
  }
}
