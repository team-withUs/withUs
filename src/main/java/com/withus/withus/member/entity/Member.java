package com.withus.withus.member.entity;

import com.withus.withus.club.entity.ClubMember;
import com.withus.withus.global.timestamp.TimeStamp;
import com.withus.withus.member.dto.UpdateRequestDto;
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
    private String introduction;

    @Column
    private String filename;

    @Column
    private Boolean isActive = true;

    @OneToMany(mappedBy = "member")
    private List<ClubMember> clubMemberList = new ArrayList<>();

    @Builder
    public Member(String loginname, String password, String email, String username) {
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

    public void update(UpdateRequestDto updateRequestDto, String password, String filename) {
        this.password = password;
        this.username = updateRequestDto.username();
        this.email = updateRequestDto.email();
        this.introduction = updateRequestDto.introduction();
        this.filename = filename;
    }

    public void inactive() {
        this.isActive = false;
    }

}
