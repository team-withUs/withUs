package com.withus.withus.global.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.withus.withus.global.security.CustomLogoutHandler;
import com.withus.withus.global.security.UserDetailsServiceImpl;
import com.withus.withus.global.security.jwt.JwtAuthenticationFilter;
import com.withus.withus.global.security.jwt.JwtAuthorizationFilter;
import com.withus.withus.global.security.jwt.JwtUtil;
import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity // Spring Security 지원을 가능하게 함
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

  private final JwtUtil jwtUtil;
  private final UserDetailsServiceImpl userDetailsService;
  private final AuthenticationConfiguration authenticationConfiguration;

  private final ObjectMapper objectMapper;

  private final CustomLogoutHandler logoutHandler;

  public WebSecurityConfig(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, AuthenticationConfiguration authenticationConfiguration, ObjectMapper objectMapper,
      CustomLogoutHandler logoutHandler) {
    this.jwtUtil = jwtUtil;
    this.userDetailsService = userDetailsService;
    this.authenticationConfiguration = authenticationConfiguration;
    this.objectMapper = objectMapper;
    this.logoutHandler = logoutHandler;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
    return configuration.getAuthenticationManager();
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
    JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil);
    filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
    return filter;
  }

  @Bean
  public JwtAuthorizationFilter jwtAuthorizationFilter() {
    return new JwtAuthorizationFilter(jwtUtil, userDetailsService, objectMapper);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    // CSRF 설정
    http.csrf((csrf) -> csrf.disable());

    // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
    http.sessionManagement((sessionManagement) ->
        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    );

    http.authorizeHttpRequests((authorizeHttpRequests) ->
        authorizeHttpRequests
            .requestMatchers("/api/member/signup/**", "/api/member/login")
            .permitAll()// 회원가입, 로그인요청 인증허가
            .requestMatchers("/ws/**", "/**")
            .permitAll()
            .requestMatchers(HttpMethod.GET,"/api/member/**")
            .permitAll()
            .anyRequest().authenticated() // 그 외 모든 요청 인증처리
    );

    http.logout()
        .logoutUrl("/api/member/logout")
        .deleteCookies("refreshtoken")
        .addLogoutHandler(logoutHandler)
        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK ));



    // 필터 관리
    http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
    http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);



    return http.build();
  }


}
