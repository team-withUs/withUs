package com.withus.withus.club.dto;

import com.withus.withus.club.entity.Club;
import lombok.Builder;

import java.time.LocalDateTime;

public record ClubResponseDto(
        Long clubId,
        String clubTitle,
        String content,
        String category,
        String image,
        String username,
        int maxMember,
        int report,
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
            String image,
            String username,
            int maxMember,
            int report,
            LocalDateTime startTime,
            LocalDateTime endTime,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt
    ) {
        this.clubId = clubId;
        this.clubTitle = clubTitle;
        this.content = content;
        this.category = category;
        this.image = image;
        this.username = username;
        this.maxMember = maxMember;
        this.report = report;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static ClubResponseDto createClubResponseDto(
            Club club
    ) {
        String username = club.getMember().getUsername();
        return ClubResponseDto.builder()
                .clubId(club.getId())
                .clubTitle(club.getClubTitle())
                .content(club.getContent())
                .category(club.getCategory().name())
                .image(club.getImage())
                .username(username)
                .maxMember(club.getMaxMember())
                .report(club.getReport())
                .startTime(club.getStartTime())
                .endTime(club.getEndTime())
                .createdAt(club.getCreatedAt())
                .modifiedAt(club.getModifiedAt())
                .build();
    }
}