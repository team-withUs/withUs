package com.withus.withus.global.security.jwt;

import com.withus.withus.global.timestamp.TimeStamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Table(name = "refreshTokens")
@NoArgsConstructor
public class RefreshToken extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long refreshTokenId;

    @Column(nullable = false)
    private String refreshToken;

    @Column(nullable = false)
    private String keyUsername;

    @Builder
    public RefreshToken(String refreshToken, String keyUsername) {
        this.refreshToken = refreshToken;
        this.keyUsername = keyUsername;
    }

    public static RefreshToken createRefreshToken(String refreshToken, String keyUsername) {
        return RefreshToken.builder()
                .refreshToken(refreshToken)
                .keyUsername(keyUsername)
                .build();
    }
}
