package com.withus.withus.club.repository;

import com.withus.withus.category.entity.ClubCategory;
import com.withus.withus.club.entity.Club;
import com.withus.withus.notice.dto.PageableDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ClubRepositoryQuery {

  List<Club> search(String keyWord, boolean isActive,String searchCategory, Pageable pageable, ClubCategory category);

}
