package com.withus.withus.club.entity;

import com.withus.withus.club.dto.ReportClubRequestDto;
import com.withus.withus.club.dto.ReportClubResponseDto;
import com.withus.withus.global.timestamp.TimeStamp;
import com.withus.withus.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ReportClub extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @Builder
    public ReportClub(String content, Club club, Member member){
        this.content = content;
        this.club = club;
        this.member = member;
    }

    public static ReportClub reportClub(
            ReportClubRequestDto reportClubRequestDto,
            Member member,
            Club club
    ) {
        String content = reportClubRequestDto.content();
        return ReportClub.builder()
                .content(content)
                .club(club)
                .member(member)
                .build();
    }
}
