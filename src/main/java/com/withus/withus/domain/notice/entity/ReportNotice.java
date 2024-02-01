package com.withus.withus.domain.notice.entity;

import com.withus.withus.domain.member.entity.Member;
import com.withus.withus.domain.notice.dto.ReportRequestDto;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ReportNotice {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String content;

  @ManyToOne
  @JoinColumn(name="member_id", nullable = false)
  private Member member;

  @ManyToOne
  @JoinColumn(name = "notice_id", nullable = false)
  private Notice notice;

  @Builder
  public ReportNotice(String content, Member member, Notice notice){
    this.content = content;
    this.member = member;
    this.notice = notice;
  }

  public static ReportNotice createReport(
      ReportRequestDto requestDto,
      Member member,
      Notice notice
  ) {

    return ReportNotice.builder()
        .content(requestDto.content())
        .member(member)
        .notice(notice)
        .build();
  }

}
