package com.withus.withus.domain.club.repository;

import com.withus.withus.domain.club.entity.ClubCategory;
import com.withus.withus.domain.club.entity.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClubRepositoryQuery {

  Page<Club> search(String keyWord, boolean isActive,String searchCategory, Pageable pageable, ClubCategory category);

}
