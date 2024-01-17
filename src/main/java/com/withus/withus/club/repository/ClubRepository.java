package com.withus.withus.club.repository;

import com.withus.withus.category.entity.ClubCategory;
import com.withus.withus.club.entity.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ClubRepository extends JpaRepository<Club, Long> {

    Optional<Club> findByIsActiveAndId(boolean isActive, Long clubId);
  
    boolean existsByIsActiveAndId(boolean isActive, Long clubId);

    List<Club> findByCategoryAndIsActive(ClubCategory category, boolean isActive, Pageable pageable);
    List<Club> findAllByIsActive(boolean isActive, Pageable pageable);

    Integer countByIsActive(boolean isActive);
}

