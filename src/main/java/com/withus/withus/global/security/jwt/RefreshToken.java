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
    private String keyLoginname;

    @Builder
    public RefreshToken(String refreshToken, String keyLoginname) {
        this.refreshToken = refreshToken;
        this.keyLoginname = keyLoginname;
    }

    public static RefreshToken createRefreshToken(String refreshToken, String keyLoginname) {
        return RefreshToken.builder()
                .refreshToken(refreshToken)
                .keyLoginname(keyLoginname)
                .build();
    }
}
