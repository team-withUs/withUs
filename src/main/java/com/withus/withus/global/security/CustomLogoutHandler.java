package com.withus.withus.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.withus.withus.global.response.CommonResponse;
import com.withus.withus.global.response.ResponseCode;
import com.withus.withus.global.security.jwt.JwtUtil;
import com.withus.withus.global.utils.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j(topic = "logout 핸들러")
public class CustomLogoutHandler implements LogoutHandler {

  private final ObjectMapper objectMapper;

  private final RedisService redisService;

  private final JwtUtil jwtUtil;

  public CustomLogoutHandler(
      ObjectMapper objectMapper, RedisService redisService, JwtUtil jwtUtil
  ) {
    this.objectMapper = objectMapper;
    this.redisService = redisService;
    this.jwtUtil = jwtUtil;
  }



  @Override
  @Transactional
  public void logout(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    String accessToken = jwtUtil.getTokenFromRequest("accessToken", request);
    try {
      if (!jwtUtil.validateToken(accessToken)) {
        log.error("유효하지않은 accesToken");
        response.setStatus(403);
        response.setCharacterEncoding("utf-8");
        try {
          PrintWriter writer = response.getWriter();
          writer.println(" 403 : Forbidden");
          writer.println("유효하지 않은 accessToken입니다.");
        } catch (IOException ex) {
          throw new RuntimeException(ex);
        }
        return;
      }
    } catch (ExpiredJwtException e) {
      log.error("만료된 accesToken");
      response.setStatus(403);
      response.setCharacterEncoding("utf-8");
      try {
        PrintWriter writer = response.getWriter();
        writer.println(" 403 : Forbidden");
        writer.println("만료된 accessToken입니다.");
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
      return;
    }

    Claims member = jwtUtil.getUserInfoFromToken(accessToken);
    String loginname = member.getSubject();

    if (!jwtUtil.existTokenByLoginname(loginname)) {
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

    // Redis에 저장된 토큰 삭제
    jwtUtil.deleteTokenInRedis(loginname);

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("utf-8");

    try {
      String jsonResponse = objectMapper.writeValueAsString(CommonResponse.of(ResponseCode.LOGOUT, ""));
      response.getWriter().write(jsonResponse);

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
