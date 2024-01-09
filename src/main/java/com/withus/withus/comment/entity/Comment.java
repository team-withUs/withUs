package com.withus.withus.comment.entity;

import com.withus.withus.global.timestamp.Timestamp;
import com.withus.withus.member.entity.Member;
import com.withus.withus.notice.entity.Notice;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends Timestamp {

    // Comment의 PK index라고 이해하면 되나?
    // @GeneratedValue(strategy = GenerationType.IDENTITY)라고 쓰면 기본 키 생성을 데이터베이스가 알아서한다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column(nullable = false)은 Comment객체와 DB테이블 null 허용 X
    @Column(nullable = false)
    private String content;

    // 게시판은 여러개의 댓글을 가질 수 있다. (댓글입장 다대일), 지연로딩, 즉시로딩 거의 안씀
    // nullable = false면 게시판없는 댓글은 없다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id", nullable = false)
    private Notice notice;

    // 멤버는 댓글을 여러개 쓸 수 있다. (댓글입장 다대일)
    // nullable = false면 작성자(멤버)없는 댓글은 없다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // report를 entity로 분리하던지, 걍 빼버리던가.
    @Column
    private int report = 0;

    @Column
    private Boolean isActive;

    // shift+f6 = 같은 이름을 쓰는 파라미터를 한 번에 수정 가능 !! 배움 !!
    @Builder
    public Comment(String content, Notice findNotice, Member loginMember, Boolean isActive){
        this.content = content;
        this.notice = findNotice;
        this.member = loginMember;
        this.isActive = isActive;
    }

}
