package com.withus.withus.member.entity;

import com.withus.withus.global.timestamp.TimeStamp;
import jakarta.persistence.*;
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
    private String image;

    @Column
    private int report = 0;

    @Column
    private boolean isActive = true;


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




}
