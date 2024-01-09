package com.withus.withus.club.repository;

import com.withus.withus.club.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ClubRepository extends JpaRepository<Club, Long> {

    Optional<Club> findByIsActiveAndId(boolean isActive, Long clubId);
}
