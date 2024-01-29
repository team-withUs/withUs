package com.withus.withus.domain.club.repository;

import com.withus.withus.domain.club.entity.ClubCategory;
import com.withus.withus.domain.club.entity.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ClubRepository extends JpaRepository<Club, Long>, ClubRepositoryQuery{

    Optional<Club> findByIsActiveAndId(boolean isActive, Long clubId);
  
    boolean existsByIsActiveAndId(boolean isActive, Long clubId);

    Page<Club> findByCategoryAndIsActiveAndMember_IsActive(
        ClubCategory category,
        boolean isActive,
        boolean memberIsActive,
        Pageable pageable);

    Page<Club> findAllByIsActiveAndMember_IsActive(
        boolean isActive,
        boolean memberIsActive,
        Pageable pageable
    );

    Integer countByIsActive(boolean isActive);


    //삭제 스캐줄러용
    List<Club> findAllByIsActive(boolean isActive);
    List<Club> findAllByMemberId(Long memberId);
}

