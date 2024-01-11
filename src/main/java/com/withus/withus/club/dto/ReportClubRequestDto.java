package com.withus.withus.club.dto;

import lombok.Builder;

public record ReportClubRequestDto(
        String content
) {
    @Builder
    public ReportClubRequestDto(
            String content
    ) {
        this.content = content;
    }
}
