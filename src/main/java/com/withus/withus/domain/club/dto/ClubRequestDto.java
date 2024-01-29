package com.withus.withus.domain.club.dto;

import com.withus.withus.domain.club.entity.ClubCategory;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Builder

public record ClubRequestDto(
        String clubTitle,
        String content,
        ClubCategory category,
        String categoryKrName,
        MultipartFile imageFile,
        LocalDateTime startTime,
        LocalDateTime endTime
) {

}