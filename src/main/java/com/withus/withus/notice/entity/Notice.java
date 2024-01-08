package com.withus.withus.notice.entity;

import com.withus.withus.club.entity.Club;
import com.withus.withus.global.timestamp.TimeStamp;
import com.withus.withus.notice.dto.NoticeRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name="notices")
public class Notice extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;

    @Column
    private String image;

    @Column
    private Integer report = 0;

    @Column(nullable = false)
    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "Club_id", nullable = false)
    private Club club;


    @Builder
    public Notice(String title,String content,
                  Long memberId,Club club){
        this.title=title;
        this.content=content;
        this.memberId=memberId;
        this.club=club;
    }

    public void update(NoticeRequestDto requestDto){
        this.title=title;
        this.content=content;
    }







}
