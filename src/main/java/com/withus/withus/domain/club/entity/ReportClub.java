package com.withus.withus.domain.club.entity;

import com.withus.withus.domain.club.dto.ReportClubRequestDto;
import com.withus.withus.domain.member.entity.Member;
import com.withus.withus.global.timestamp.TimeStamp;
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
    @JoinColumn(name = "user_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @Builder
    public ReportClub(String content, Club club, Member member){
        this.content = content;
        this.club = club;
        this.member = member;
    }

    public static ReportClub createReport(
        ReportClubRequestDto reportClubRequestDto,
        Member member,
        Club club
    ) {

        return ReportClub.builder()
                .content(reportClubRequestDto.content())
                .member(member)
                .club(club)
                .build();
    }
}
