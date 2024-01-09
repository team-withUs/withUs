package com.withus.withus.club.repository;

import com.withus.withus.club.entity.ClubMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {
}
