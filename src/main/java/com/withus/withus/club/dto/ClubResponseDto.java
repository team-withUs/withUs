package com.withus.withus.club.dto;

import com.withus.withus.category.entity.ClubCategory;
import com.withus.withus.club.entity.Club;
import com.withus.withus.global.s3.S3Const;
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
        int maxMember,
        LocalDateTime startTime,
        LocalDateTime endTime,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    public static ClubResponseDto createClubResponseDto(
            Club club,
            String imageURL
    ) {
        String imageUrl = imageURL;
        return ClubResponseDto.builder()
                .clubId(club.getId())
                .clubTitle(club.getClubTitle())
                .content(club.getContent())
                .category(club.getCategory())
                .imageURL(imageUrl)
                .username(club.getUsername())
                .maxMember(club.getMaxMember())
                .startTime(club.getStartTime())
                .endTime(club.getEndTime())
                .createdAt(club.getCreatedAt())
                .modifiedAt(club.getModifiedAt())
                .build();
    }

    public static ClubResponseDto createClubResponseDto(Club club) {
        return createClubResponseDto(club, S3Const.S3_DIR_CLUB);
    }
}