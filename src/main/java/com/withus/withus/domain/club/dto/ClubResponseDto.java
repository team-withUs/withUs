package com.withus.withus.domain.club.dto;

import com.withus.withus.domain.club.entity.ClubCategory;
import com.withus.withus.domain.club.entity.Club;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ClubResponseDto(
        Long clubId,
        String clubTitle,
        String content,
        ClubCategory category,
        String imageURL,
        String username,
        LocalDateTime startTime,
        LocalDateTime endTime,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    public static ClubResponseDto createClubResponseDto(
            Club club
    ) {

        return ClubResponseDto.builder()
                .clubId(club.getId())
                .clubTitle(club.getClubTitle())
                .content(club.getContent())
                .category(club.getCategory())
                .imageURL(club.getImageUrl())
                .username(club.getUsername())
                .startTime(club.getStartTime())
                .endTime(club.getEndTime())
                .createdAt(club.getCreatedAt())
                .modifiedAt(club.getModifiedAt())
                .build();
    }
}