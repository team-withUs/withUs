package com.withus.withus.club.entity;

import com.withus.withus.global.timestamp.Timestamp;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "club")
public class Club extends Timestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String clubTitle;

    @Column(nullable = false)
    private String content;

    @Column
    private String image;

    @Column
    private int report = 0;

    @Builder
    public Club(String clubTitle, String content){
        this.clubTitle = clubTitle;
        this.content = content;
    }
}
