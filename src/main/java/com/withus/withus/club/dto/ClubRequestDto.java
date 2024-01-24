package com.withus.withus.club.dto;

import com.withus.withus.category.entity.ClubCategory;
import lombok.Builder;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
@Builder

public record ClubRequestDto(
        String clubTitle,
        String content,
        ClubCategory category,
        MultipartFile imageFile,
        LocalDateTime startTime,
        LocalDateTime endTime
) {

}