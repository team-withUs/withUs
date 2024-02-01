package com.withus.withus.domain.club.dto;

import com.withus.withus.domain.club.entity.Club;
import com.withus.withus.domain.club.entity.ReportClub;
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
//        String content = reportClub.getContent();

        return ReportClubResponseDto.builder()
                .clubId(club.getId())
                .username(club.getMember().getUsername())
                .content(reportClub.getContent())
                .build();
    }
}
