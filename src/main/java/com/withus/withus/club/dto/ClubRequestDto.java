package com.withus.withus.club.dto;

import com.withus.withus.category.entity.ClubCategory;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public record ClubRequestDto(
        String clubTitle,
        String content,
        ClubCategory category,
        MultipartFile imageFile,
        int maxMember,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
    @Builder
    public ClubRequestDto(
            String clubTitle,
            String content,
            ClubCategory category,
            MultipartFile imageFile,
            int maxMember,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        this.clubTitle = clubTitle;
        this.content = content;
        this.category = category;
        this.imageFile = imageFile;
        this.maxMember = maxMember;
        this.startTime = startTime;
        this.endTime = endTime;
    }


}