package com.withus.withus.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.withus.withus.global.response.exception.ErrorCode;
import com.withus.withus.global.response.exception.ExceptionResponseDto;
import com.withus.withus.global.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final ObjectMapper objectMapper;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService,
                                  ObjectMapper objectMapper) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
                                    FilterChain filterChain) throws ServletException, IOException {

        String accessTokenValue = jwtUtil.getTokenFromRequest("accessToken", req);
        String accessToken = "";
        if (StringUtils.hasText(accessTokenValue)) {
            log.info(accessTokenValue);

            try {
                if (!jwtUtil.validateToken(accessTokenValue)) {
                    log.error("유효하지않은 accesToken");
                    setResponse(res, ErrorCode.ACCESS_DENIED);

                    return;
                }

                if (!jwtUtil.existTokenInRedis(accessTokenValue, "access")) {
                    log.error("로그아웃한 멤버입니다. 다시 로그인 해주세요");
                    setResponse(res, ErrorCode.LOGOUT_USER);

                    return;
                }

                if (jwtUtil.isDuplicateLogin(accessTokenValue)) {
                    deleteCookie(req, res);
                    log.error("이미 로그인한 멤버입니다. 다시 로그인 해주세요.");
                    setResponse(res, ErrorCode.DUPLICATE_LOGIN);
                    return;
                }

            } catch (ExpiredJwtException e) {
                // 만료된 accessToken 일경우 accessToken 재발급
                // 쿠키에서 refreshToken 가져와서 유효성 검사 후  발급
                String refreshToken = jwtUtil.getTokenFromRequest("refreshToken", req);
                if (refreshToken == null) {
                    log.error("쿠키에 refreshToken이 존재하지 않습니다.");
                    setResponse(res, ErrorCode.ACCESS_DENIED);

                    return;
                }

                // refreshToken 검증
                try {
                    if (!jwtUtil.validateToken(refreshToken)) {
                        log.error("유효하지않은 RefreshToken");
                        setResponse(res, ErrorCode.ACCESS_DENIED);

                        return;
                    }
                } catch (ExpiredJwtException exception) {
                    log.error("만료된 RefreshToken");
                    setResponse(res, ErrorCode.EXPIRED_TOKEN);

                    return;
                }

                // refreshToken DB 조회
                if (!jwtUtil.existTokenInRedis(refreshToken, "refresh")) {
                    log.error("DB에 해당 RefreshToken이 존재하지 않습니다.");
                    setResponse(res, ErrorCode.ACCESS_DENIED);
                    return;
                }

                // accessToken 재발급
                accessToken = jwtUtil.reissuanceAccessToken(refreshToken);
                log.info("accessToken 재발급");

                // accessToken 쿠키에 저장
                jwtUtil.addJwtToCookie("accessToken", accessToken, res);

            }

            if (!accessToken.isBlank()) {
                accessTokenValue = accessToken;
            }

            Claims info = jwtUtil.getUserInfoFromToken(accessTokenValue);

            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                setResponse(res, ErrorCode.NOT_FOUND_MEMBER);

                return;
            }
        }

        filterChain.doFilter(req, res);
    }

    // 인증 처리
    public void setAuthentication(String loginname) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(loginname);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String loginname) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginname);
        return new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
    }

    private void setResponse(HttpServletResponse response, ErrorCode errorCode) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(errorCode.getStatus().value());
        response.setCharacterEncoding("utf-8");
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(errorCode);

        try {
            response.getWriter().write(objectMapper.writeValueAsString(exceptionResponseDto));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void deleteCookie(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {

                if (cookie.getName().equals("accessToken") || cookie.getName().equals("refreshToken")) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

}