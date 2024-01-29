package com.withus.withus.domain.comment.entity;

import com.withus.withus.domain.comment.dto.ReportRequestDto;
import com.withus.withus.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class ReportComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String content;

    @Builder
    public ReportComment(String content, Member loginMember, Comment reportComment){
        this.content = content;
        this.member = loginMember;
        this.comment = reportComment;
    }

    public static ReportComment createReport(ReportRequestDto requestDto, Member member, Comment comment) {
        String content = requestDto.content();

        return ReportComment.builder()
                .content(content)
                .loginMember(member)
                .reportComment(comment)
                .build();
    }
}
