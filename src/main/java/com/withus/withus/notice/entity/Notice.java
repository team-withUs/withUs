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

    private Boolean isActive=true;

//    @Column(nullable = false)
//    private Long memberId;

//    @ManyToOne
//    @JoinColumn(name = "Club_id", nullable = false)
//    private Club club;


    @Builder
    public Notice(String title, String content){
        this.title = title;
        this.content = content;
//        this.memberId=memberId;
//        this.club=club;
    }

    public void update(NoticeRequestDto requestDto){
        this.title = requestDto.title();
        this.content = requestDto.content();
    }
    public void updateReport(Integer report){
        this.report=report;
    }

    public void delete(Boolean isActive){
        this.isActive=isActive;
    }

    public static Notice createNotice(NoticeRequestDto requestDto){
        String title = requestDto.title();
        String content = requestDto.content();

        return Notice.builder()
            .title(title)
            .content(content)
            .build();
    }







}
