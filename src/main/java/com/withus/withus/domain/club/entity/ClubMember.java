package com.withus.withus.domain.club.entity;

import com.withus.withus.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class ClubMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userClub_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column
    private ClubMemberRole clubMemberRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public ClubMember(Club club, Member member, ClubMemberRole clubMemberRole) {
        this.club = club;
        this.member = member;
        this.clubMemberRole = clubMemberRole;
    }

    public static ClubMember createClubMember(Club club, Member member, ClubMemberRole clubMemberRole){
        return ClubMember.builder()
            .club(club)
            .member(member)
            .clubMemberRole(clubMemberRole)
            .build();
    }

}