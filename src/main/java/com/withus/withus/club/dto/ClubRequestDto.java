package com.withus.withus.club.dto;

import com.withus.withus.category.entity.ClubCategory;

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
    public ClubRequestDto(
            String clubTitle,
            String content,
            String category,
            String image,
            int maxMember,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        this(clubTitle, content, ClubCategory.valueOf(category.toUpperCase()), image, maxMember, startTime, endTime);
    }
}