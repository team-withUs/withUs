package com.withus.withus.club.dto;

import com.withus.withus.category.entity.ClubCategory;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDateTime;


public record ClubRequestDto(
        @Size(min = 1, max = 50) String clubTitle,
        @Size(min = 1, max = 50) String content,
        ClubCategory category,
        String image,
        int maxMember,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
    public ClubRequestDto(
            @Size(min = 1, max = 50) String clubTitle,
            @Size(min = 1, max = 50) String content,
            String category,
            String image,
            int maxMember,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        this(clubTitle, content, ClubCategory.valueOf(category.toUpperCase()), image, maxMember, startTime, endTime);
    }
}