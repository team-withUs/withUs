package com.withus.withus.global.security.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.withus.withus.global.security.UserDetailsImpl;
import com.withus.withus.member.dto.LoginRequestDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/member/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(),
                LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                    requestDto.loginname(),
                    requestDto.password(),
                    null
                )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException, ServletException {
        log.info("로그인 성공 및 JWT 생성");
        String loginname = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();

        // 로그인시 기 발급된 토큰이 존재하면 해당 토큰 무효화 처리
        // AccessToken, RefreshToken Redis삭제
        jwtUtil.deleteTokenInRedis(loginname);

        String accessToken = jwtUtil.createAccessToken(loginname);
        String refreshToken = jwtUtil.createRefreshToken(loginname);

        jwtUtil.saveJwtToRedis(accessToken, refreshToken, loginname);

        // RefreshToken 쿠키에 저장
        jwtUtil.addJwtToCookie("refreshToken", refreshToken, response);

        // AccessToken 쿠키에 저장
        jwtUtil.addJwtToCookie("accessToken", accessToken, response);

        response.setStatus(200);
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        writer.println(" 200 : Ok");
        writer.println("로그인에 성공하였습니다.");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, AuthenticationException failed)
        throws IOException, ServletException {
        log.info("로그인 실패");

        response.setStatus(400);
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        writer.println(" 400 : Bad Request");
        writer.println("loginname 또는 password를 확인해주세요.");

    }
}