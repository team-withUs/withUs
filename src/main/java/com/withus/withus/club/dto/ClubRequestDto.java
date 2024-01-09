package com.withus.withus.club.dto;

import com.withus.withus.category.entity.ClubCategory;
import lombok.Builder;

import java.time.LocalDateTime;

public record ClubRequestDto(
        String clubTitle,
        String content,
        ClubCategory category,
        String image,
        int maxMember,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
    @Builder
    public ClubRequestDto(
            String clubTitle,
            String content,
            ClubCategory category,
            String image,
            int maxMember,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        this.clubTitle = clubTitle;
        this.content = content;
        this.category = category;
        this.image = image;
        this.maxMember = maxMember;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}