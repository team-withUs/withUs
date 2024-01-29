package com.withus.withus.domain.comment.entity;

import com.withus.withus.domain.comment.dto.ReportRequestDto;
import com.withus.withus.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ReportComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public ReportComment(String content, Member loginMember, Comment reportComment){
        this.content = content;
        this.member = loginMember;
        this.comment = reportComment;
    }

    public static ReportComment createReport(
        ReportRequestDto requestDto,
        Member member,
        Comment comment
    ) {

        return ReportComment.builder()
                .content(requestDto.content())
                .loginMember(member)
                .reportComment(comment)
                .build();
    }

}
