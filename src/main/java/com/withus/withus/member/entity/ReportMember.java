package com.withus.withus.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ReportMember {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long reporterId;

  @Column(nullable = false)
  private Long reportedId;

  @Column
  private String content;

  @Builder
  public ReportMember(Long reporterId, Long reportedId, String content){
    this.reporterId = reporterId;
    this.reportedId = reportedId;
    this.content = content;
  }

  public static ReportMember createReportMember(Long reporterId, Long reportedId, String content){
    return ReportMember.builder()
        .reporterId(reporterId)
        .reportedId(reportedId)
        .content(content)
        .build();
  }
}
