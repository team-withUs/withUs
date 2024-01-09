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
public class Club extends TimeStamp {
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
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClubCategory category;

    private boolean isActive = true;



@Builder
public Club(String clubTitle, String content, String category, int maxMember ,Member member, String image,LocalDateTime startTime, LocalDateTime endTime) {
    this.member = member;
    this.clubTitle = clubTitle;
    this.content = content;
    this.category = ClubCategory.valueOf(category);
    this.image = image;
    this.MaxMember = maxMember;
    this.startTime = startTime;
    this.endTime = endTime;
}

    public static Club createClub(ClubRequestDto clubRequestDto, Member member, LocalDateTime startTime, LocalDateTime endTime) {
        String clubTitle = clubRequestDto.clubTitle();
        String content = clubRequestDto.clubTitle();
        String category = String.valueOf(clubRequestDto.category());
        String image = clubRequestDto.image();
        int MaxMember = clubRequestDto.maxMember();
        LocalDateTime startedTime= clubRequestDto.startTime();
        LocalDateTime endedTime = clubRequestDto.endTime();

        return Club.builder()
                .member(member)
                .clubTitle(clubTitle)
                .content(content)
                .category(category)
                .image(image)
                .maxMember(MaxMember)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }


    public void update(ClubRequestDto clubrequestDto) {
        this.clubTitle = clubrequestDto.clubTitle();
        this.content = clubrequestDto.content();
        this.category = clubrequestDto.category();
        this.image = clubrequestDto.image();
        this.MaxMember = clubrequestDto.maxMember();
        this.startTime =clubrequestDto.startTime();
        this.endTime = clubrequestDto.endTime();
    }

    public void delete() {
        this.isActive=false;
    }

    public void updateReport(int report) {
        this.report=report;
    }
}