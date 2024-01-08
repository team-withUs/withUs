package com.withus.withus.global.security;

import com.withus.withus.global.response.ResponseCode;
import com.withus.withus.global.security.jwt.JwtUtil;
import com.withus.withus.global.security.jwt.RefreshTokenRepository;
import com.withus.withus.global.utils.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j(topic = "logout 핸들러")
public class CustomLogoutHandler implements LogoutHandler {
  private final RefreshTokenRepository refreshTokenRepository;

  private final RedisService redisService;

  private final JwtUtil jwtUtil;

  public CustomLogoutHandler(RefreshTokenRepository refreshTokenRepository,
      RedisService redisService, JwtUtil jwtUtil) {
    this.refreshTokenRepository = refreshTokenRepository;
    this.redisService = redisService;
    this.jwtUtil = jwtUtil;
  }



  @Override
  @Transactional
  public void logout(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    String accessToken = jwtUtil.getJwtFromHeader(request);
    try {
      if (!jwtUtil.validateToken(accessToken)) {
        log.error("유효하지않은 AccesToken");
        response.setStatus(403);
        response.setCharacterEncoding("utf-8");
        try {
          PrintWriter writer = response.getWriter();
          writer.println(" 403 : Forbidden");
          writer.println("유효하지 않은 AccessToken입니다.");
        } catch (IOException ex) {
          throw new RuntimeException(ex);
        }
        return;
      }
    } catch (ExpiredJwtException e) {
      log.error("만료된 AccesToken");
      response.setStatus(403);
      response.setCharacterEncoding("utf-8");
      try {
        PrintWriter writer = response.getWriter();
        writer.println(" 403 : Forbidden");
        writer.println("만료된 AccessToken입니다.");
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
      return;
    }

    Claims member = jwtUtil.getUserInfoFromToken(accessToken);
    String loginname = member.getSubject();

    if (!refreshTokenRepository.existsByKeyLoginname(loginname)) {
      log.error("이미 로그아웃한 유저");
      response.setStatus(400);
      response.setCharacterEncoding("utf-8");
      try {
        PrintWriter writer = response.getWriter();
        writer.println(" 400 : BAD REQUEST");
        writer.println("이미 로그아웃한 유저입니다.");
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
      return;
    }

    redisService.setValues(loginname, accessToken, Duration.ofMinutes(30));   // AccessToken 만료시간
    refreshTokenRepository.deleteByKeyLoginname(loginname);

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(ResponseCode.LOGOUT.getHttpStatus());
    response.setCharacterEncoding("utf-8");
    try {
      response.getWriter().write("HttpStatus" + ":" + ResponseCode.LOGOUT.getHttpStatus() + "\n" + ResponseCode.LOGOUT.getMessage());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
