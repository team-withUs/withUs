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

        String accessToken = jwtUtil.createAccessToken(loginname);
        String refreshToken = "";

        try {
            //  Http 로그인 URL 요청시 토큰저장소 조회 **
            RefreshToken refreshTokenIns = jwtUtil.getTokenDBByLoginname(loginname);
            refreshToken = refreshTokenIns.getRefreshToken();

        } catch (NullPointerException e) {
            refreshToken = jwtUtil.createRefreshToken(loginname);
            // RefreshToken DB에 저장
            jwtUtil.saveRefreshJwtToDB(refreshToken, loginname);
        }

        // RefreshToken 쿠키에 저장
        jwtUtil.addJwtToCookie(refreshToken, response);

        // AccessToken 헤더에 저장
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);

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
        writer.println("username 또는 password를 확인해주세요.");

    }
}