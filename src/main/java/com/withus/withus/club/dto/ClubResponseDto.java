package com.withus.withus.club.dto;

import com.withus.withus.club.entity.Club;
import lombok.Builder;

import java.time.LocalDateTime;


public record ClubResponseDto(
        Long clubId,
        String clubTitle,
        String memberId,
        String content,
        String category,
        String image,
        int maxMember,
        int report,
        LocalDateTime startTime,
        LocalDateTime endTime,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    @Builder
    public ClubResponseDto(Club club) {

        this(club.getId(),
                club.getClubTitle(),
                club.getMember().getUsername(),
                club.getContent(),
                club.getCategory().name(),
                club.getImage(),
                club.getMaxMember(),
                club.getReport(),
                club.getStartTime(),
                club.getEndTime(),
                club.getCreatedAt(),
                club.getModifiedAt());
    }
}