package com.withus.withus.club.dto;

import com.withus.withus.club.entity.Club;
import com.withus.withus.club.entity.ReportClub;
import lombok.Builder;
@Builder
public record ReportClubResponseDto(
        Long clubId,
        String username,
        String content
) {
    public static ReportClubResponseDto createReportClubResponseDto(
            Club club,
            ReportClub reportClub
    ) {
        String username = club.getUsername();
        String content = reportClub.getContent();
        return ReportClubResponseDto.builder()
                .clubId(club.getId())
                .username(username)
                .content(reportClub.getContent())
                .build();
    }
}
