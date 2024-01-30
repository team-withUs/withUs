package com.withus.withus.notice.entity;

import com.withus.withus.club.entity.Club;
import com.withus.withus.global.timestamp.TimeStamp;
import com.withus.withus.member.entity.Member;
import com.withus.withus.notice.dto.NoticeRequestDto;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Notice extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NoticeCategory category;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000, nullable = false)
    private String content;

    @Column
    private String imageURL;

    @Column
    private String filename;

    @Column
    private Boolean isActive = true;

    @ManyToOne
    @JoinColumn(name="member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "Club_id", nullable = false)
    private Club club;

    @Builder
    private Notice(
        String title,
        String content,
        Member member,
        Club club,
        NoticeCategory category,
        String imageURL,
        String filename
    ) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.club = club;
        this.category = category;
        this.imageURL = imageURL;
        this.filename = filename;
    }

    public void update(NoticeRequestDto requestDto, NoticeCategory category) {
        this.title = requestDto.title();
        this.content = requestDto.content();
        this.category = category;
    }

    public void updatePlusImage(
        NoticeRequestDto requestDto,
        NoticeCategory category,
        String imageURL,
        String filename
    ) {
        this.title = requestDto.title();
        this.content = requestDto.content();
        this.category = category;
        this.imageURL = imageURL;
        this.filename = filename;
    }

    public void inActive(){
        this.isActive=false;
    }

    public static Notice createNotice(
        NoticeRequestDto requestDto,
        Member member,
        Club club,
        NoticeCategory category
    ) {

      return Notice.builder()
          .title(title)
          .content(content)
          .member(member)
          .club(club)
          .category(category)
          .build();
    }

    public static Notice createNoticePlusImage(
        NoticeRequestDto requestDto,
        Member member,
        Club club,
        NoticeCategory category,
        String imageURL,
        String filename
    ) {

        return Notice.builder()
            .title(title)
            .content(content)
            .member(member)
            .club(club)
            .category(category)
            .imageURL(imageURL)
            .filename(filename)
            .build();
    }

}
