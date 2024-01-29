package com.withus.withus.domain.club.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.withus.withus.domain.club.entity.ClubCategory;
import com.withus.withus.domain.club.entity.Club;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import static com.withus.withus.domain.club.entity.QClub.club;

@RequiredArgsConstructor
public class ClubRepositoryQueryImpl implements ClubRepositoryQuery {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Page<Club> search(String keyWord, boolean isActive,String searchCategory, Pageable pageable, ClubCategory category) {
    List<Club> list;
    if(category.equals(ClubCategory.ALL)){
      if(searchCategory.equals("content")){
        System.out.println(category+"============1============="+searchCategory);
        list = jpaQueryFactory
            .select(club)
            .from(club)
            .where(club.isActive.eq(true),club.member.isActive.eq(true),containsSearchContent(keyWord))
            .orderBy(club.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
      }
      else {  //title
        System.out.println(category+"===========2=============="+searchCategory);
        list = jpaQueryFactory
            .select(club)
            .from(club)
            .where(club.isActive.eq(true),club.member.isActive.eq(true),containsSearchTitle(keyWord))
            .orderBy(club.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
      }

    }
    else {
      if(searchCategory.equals("content")){
        System.out.println(category+"============3============="+searchCategory);
        list = jpaQueryFactory
            .select(club)
            .from(club)
            .where(club.isActive.eq(true),club.member.isActive.eq(true),club.category.eq(category),containsSearchContent(keyWord))
            .orderBy(club.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
      }
      else {
        System.out.println(category+"============4============="+searchCategory);
        list = jpaQueryFactory
            .select(club)
            .from(club)
            .where(club.isActive.eq(true),club.member.isActive.eq(true),club.category.eq(category),containsSearchTitle(keyWord))
            .orderBy(club.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
      }
    }
    return new PageImpl<>(list,pageable,countTotalRecords(keyWord, isActive, searchCategory, category)) {
    };
  }

  private BooleanExpression containsSearchTitle(String keyWord){
    return keyWord != null ? club.clubTitle.contains(keyWord) : null;
  }

  private BooleanExpression containsSearchContent(String keyWord){
    return keyWord != null ? club.content.contains(keyWord) : null;
  }

  private long countTotalRecords(String keyWord, boolean isActive, String searchCategory, ClubCategory category) {
    return jpaQueryFactory
        .selectFrom(club)
        .where(club.isActive.eq(true),
            category.equals(ClubCategory.ALL) ? null : club.category.eq(category),
            searchCategory.equals("content") ? containsSearchContent(keyWord) : containsSearchTitle(keyWord))
        .fetchCount();
  }

}
