package com.withus.withus.club.entity;

import com.withus.withus.category.entity.ClubCategory;
import com.withus.withus.club.dto.ClubRequestDto;
import com.withus.withus.global.timestamp.TimeStamp;
import com.withus.withus.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name ="club")
public class Club extends TimeStamp{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String clubTitle;

    @Column(nullable = false)
    private String content;

    @Column
    private String image;

    @Column
    private int MaxMember = 0;

    @Column
    private int report = 0;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClubCategory category;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;
    public void setMember(Member member) {
        this.member = member;
    }
    @Builder
    public Club(ClubRequestDto requestDto, Member member, LocalDateTime startTime, LocalDateTime endTime) {
        this.member = member;
        this.clubTitle = requestDto.getClubTitle();
        this.content = requestDto.getContent();
        this.category = requestDto.getCategoryAsEnum();
        this.image = requestDto.getImage();
        this.MaxMember = requestDto.getMaxMember();
        this.startTime = startTime;
        this.endTime = endTime;
    }

}
