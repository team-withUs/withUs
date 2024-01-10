package com.withus.withus.club.dto;

import com.withus.withus.club.entity.Club;
import com.withus.withus.club.entity.ReportClub;
import lombok.Builder;

public record ReportClubResponseDto(
        Long clubId,
        String username,
        String content
) {
    @Builder
    public ReportClubResponseDto(
            Long clubId,
            String username,
            String content
    ){
        this.clubId = clubId;
        this.username = username;
        this.content = content;
    }

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
