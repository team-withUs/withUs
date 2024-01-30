package com.withus.withus.domain.club.entity;

import com.withus.withus.domain.club.dto.ClubRequestDto;

import com.withus.withus.global.timestamp.TimeStamp;
import com.withus.withus.domain.member.entity.Member;
import com.withus.withus.notice.entity.Notice;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Length;

@Entity
@Getter
@NoArgsConstructor
public class Club extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ClubCategory category;

    @Column(nullable = false)
    private String clubTitle;

    @Column(length = 1000, nullable = false)
    private String content;

    @Column
    private String filename;

    @Column
    private String imageURL;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(nullable = false)
    private String username;

    private boolean isActive = true;

    @OneToMany(mappedBy = "club", cascade = CascadeType.REMOVE)
    private List<Notice> noticeList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    private Club(
            String clubTitle,
            String content,
            ClubCategory category,
            Member member,
            String filename,
            String imageURL,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        this.clubTitle = clubTitle;
        this.content = content;
        this.category = category;
        this.filename = filename;
        this.imageURL = imageURL;
        this.member = member;
        this.startTime = startTime;
        this.endTime = endTime;
        this.username = member.getUsername();
    }

    public static Club createClub(
        ClubRequestDto clubRequestDto,
        Member member,
        String filename,
        String imageURL,
        LocalDateTime startTime,
        LocalDateTime endTime
    ) {

        return Club.builder()
                .clubTitle(clubRequestDto.clubTitle())
                .content(clubRequestDto.content())
                .category(clubRequestDto.category())
                .filename(filename)
                .imageURL(imageURL)
                .member(member)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }

    public void update(
            ClubRequestDto clubrequestDto,
            String filename,
            String imageURL
    ) {
        this.clubTitle = clubrequestDto.clubTitle();
        this.content = clubrequestDto.content();
        this.category = clubrequestDto.category();
        this.filename = filename;
        this.imageURL = imageURL;
        this.startTime = clubrequestDto.startTime();
        this.endTime = clubrequestDto.endTime();
    }

    public void inActive() {
        isActive = false;
    }

    public void delete() {
        this.isActive = false;
    }

    public String setImgUrl(String imageUrl) {
        return imageUrl;
    }

    public String getImageUrl() {
        return this.imageURL;
    }


    public Member getAuthor() {
        return this.member;
    }

}