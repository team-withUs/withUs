package com.withus.withus.club.dto;

import com.withus.withus.category.entity.ClubCategory;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
@Builder
public record ClubRequestDto(
        String clubTitle,
        String content,
        ClubCategory category,
        MultipartFile imageFile,
        int maxMember,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
}