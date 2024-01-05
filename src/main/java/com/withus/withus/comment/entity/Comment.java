package com.withus.withus.comment.entity;

import com.withus.withus.global.timestamp.Timestamp;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column
    private int report = 0;

    @Builder
    public Comment(String content){
        this.content = content;
    }
}
