package com.withus.withus.global.scheduler;

import com.withus.withus.global.security.jwt.RefreshToken;
import com.withus.withus.global.security.jwt.RefreshTokenRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j(topic = "삭제 Scheduler")
@Component
@RequiredArgsConstructor
public class Scheduler {

  private final RefreshTokenRepository refreshTokenRepository;

  // 매시간 마다 만료된 토큰 자동삭제
  @Scheduled(cron = "0 0 * * * * ")
  public void logoutRefreshTokenCheck() {
    log.info("RefreshToken 삭제 실행");
    List<RefreshToken> expiredRefreshTokenList = refreshTokenRepository.findAll();
    if (!expiredRefreshTokenList.isEmpty()) {
      for (RefreshToken refreshToken : expiredRefreshTokenList) {
        LocalDateTime endTime = refreshToken.getCreatedAt().plusDays(14);
        if (LocalDateTime.now().isAfter(endTime)) {
          refreshTokenRepository.delete(refreshToken);
        }
      }
    }
  }

}