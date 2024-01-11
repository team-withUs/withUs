package com.withus.withus.club.dto;

import com.withus.withus.club.entity.Club;
import com.withus.withus.global.s3.S3Const;
import lombok.Builder;

import java.time.LocalDateTime;

public record ClubResponseDto(
        Long clubId,
        String clubTitle,
        String content,
        String category,
        String imageURL,
        String username,
        int maxMember,
        LocalDateTime startTime,
        LocalDateTime endTime,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    @Builder
    public ClubResponseDto(
            Long clubId,
            String clubTitle,
            String content,
            String category,
            String imageURL,
            String username,
            int maxMember,
            LocalDateTime startTime,
            LocalDateTime endTime,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt
    ) {
        this.clubId = clubId;
        this.clubTitle = clubTitle;
        this.content = content;
        this.category = category;
        this.imageURL = imageURL;
        this.username = username;
        this.maxMember = maxMember;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static ClubResponseDto createClubResponseDto(
            Club club,
            String imageURL
    ) {
        String imageUrl = imageURL;
        return ClubResponseDto.builder()
                .clubId(club.getId())
                .clubTitle(club.getClubTitle())
                .content(club.getContent())
                .category(club.getCategory().name())
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