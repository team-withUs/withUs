package com.withus.withus.club.dto;

import com.withus.withus.club.entity.Club;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ClubResponseDto {
    private Long clubId;
    private String clubTitle;
    private String member_id;
    private String content;
    private String category;
    private String image;
    private int maxMember;
    private int report;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ClubResponseDto(Club club){
        this.clubId = club.getId();
        this.clubTitle = club.getClubTitle();
        this.member_id = club.getMember().getUsername();
        this.content = club.getContent();
        this.category = club.getCategory().name();
        this.image = club.getImage();
        this.maxMember = club.getMaxMember();
        this.report = club.getReport();
        this.startTime = club.getStartTime();
        this.endTime = club.getEndTime();
        this.createdAt = club.getCreatedAt();
        this.modifiedAt = club.getModifiedAt();
    }
}