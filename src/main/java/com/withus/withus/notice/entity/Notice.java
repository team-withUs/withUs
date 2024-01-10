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
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;

    @Column
    private String image;


    private Boolean isActive = true;

    @ManyToOne
    @JoinColumn(name="member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "Club_id", nullable = false)
    private Club club;

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private NoticeCategory category;



    @Builder
    public Notice(String title, String content,Member member, Club club){
        this.title = title;
        this.content = content;
        this.member = member;
        this.club=club;
    }

    public void update(NoticeRequestDto requestDto){
        this.title = requestDto.title();
        this.content = requestDto.content();
    }

    public void delete(){
        this.isActive=false;
    }

    public static Notice createNotice(NoticeRequestDto requestDto, Member member, Club club){
        String title = requestDto.title();
        String content = requestDto.content();

      return Notice.builder()
          .title(title)
          .content(content)
          .member(member)
          .club(club)
          .build();
    }







}
