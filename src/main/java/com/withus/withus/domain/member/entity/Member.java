package com.withus.withus.domain.member.entity;

import com.withus.withus.domain.club.entity.ClubMember;
import com.withus.withus.domain.member.dto.UpdateRequestDto;
import com.withus.withus.global.timestamp.TimeStamp;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Member extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String loginname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String username;

    @Column
    private String tag;

    @Column
    private String introduction;

    @Column
    private String imageURL;

    @Column
    private String filename;

    @Column
    private Boolean isActive = true;

    @OneToMany(mappedBy = "member")
    private List<ClubMember> clubMemberList = new ArrayList<>();

    @Builder
    private Member(String loginname, String password, String email, String username) {
        this.loginname = loginname;
        this.password = password;
        this.email = email;
        this.username = username;
    }

    public static Member createMember(
        String loginname,
        String password,
        String email,
        String username
    ) {

        return Member.builder()
            .loginname(loginname)
            .password(password)
            .username(username)
            .email(email)
            .build();
    }

    //파일 첨부
    public void update(
        UpdateRequestDto updateRequestDto,
        String imageURL,
        String filename
    ) {
        this.username = updateRequestDto.username();
        this.email = updateRequestDto.email();
        this.introduction = updateRequestDto.introduction();
        this.tag = updateRequestDto.tag();
        this.imageURL = imageURL;
        this.filename = filename;
    }

    //파일 미첨부
    public void update(
        UpdateRequestDto updateRequestDto
    ) {
        this.username = updateRequestDto.username();
        this.email = updateRequestDto.email();
        this.introduction = updateRequestDto.introduction();
        this.tag = updateRequestDto.tag();
    }

    //비밀번호 변경
    public void updatePassword(
        String password
    ) {
        this.password = password;
    }

    public void inactive() {
        this.isActive = false;
    }

}
