package com.withus.withus.global.security.jwt;

import com.withus.withus.global.utils.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtUtil {

    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";

    // 토큰 만료시간
    private final long ACCESS_TOKEN_TIME = 30 * 60 * 1000L; // 30분
    private final long REFRESH_TOKEN_TIME = 7 * 24 * 60 * 60 * 1000L; // 1주

    @Value("${jwt.secret}") // Base64 Encode 한 SecretKey
    private String secretKey;


    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    private final RefreshTokenRepository refreshTokenRepository;

    private final RedisService redisService;

    public JwtUtil(RefreshTokenRepository refreshTokenRepository, RedisService redisService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.redisService = redisService;
    }

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // AccessToken 생성
    public String createAccessToken(String loginname) {
        Date date = new Date();

        return Jwts.builder()
                        .setSubject(loginname) // 사용자 식별자값(ID)
                        .claim(AUTHORIZATION_KEY, "USER")
                        .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    // RefreshToken 생성
    public String createRefreshToken(String loginname) {
        Date date = new Date();

        return Jwts.builder()
                .setSubject(loginname) // 사용자 식별자값(ID)
                .claim(AUTHORIZATION_KEY, "USER")
                .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME)) // 만료 시간
                .setIssuedAt(date) // 발급일
                .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                .compact();
    }


    // JWT Cookie 에 저장
    public void addJwtToCookie(String tokenName, String JWT, HttpServletResponse res) {

        Cookie cookie = new Cookie(tokenName, JWT); // Name-Value
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24); // 쿠키 유효기간 1일

        // Response 객체에 Cookie 추가
        res.addCookie(cookie);
    }

    // RefreshToken 객체 생성 및 DB 저장
    public void saveRefreshJwtToDB(String refreshToken, String loginname) {
        // RefreshToken DB에 저장
        RefreshToken refreshTokenEntity = RefreshToken.createRefreshToken(refreshToken, loginname);

        refreshTokenRepository.save(refreshTokenEntity);
    }

    // RefreshToken DB 중복 조회 검사
    public boolean checkTokenDBByToken(String token) {
        Claims user = getUserInfoFromToken(token);
        try {
            Optional<RefreshToken> refreshToken = refreshTokenRepository.findByKeyLoginname(user.getSubject());
            if (refreshToken.isPresent()) {
                return true;
            }
        } catch (IllegalArgumentException e) {
            return false;
        }

        throw new IllegalArgumentException("존재하지 않는 토큰입니다.");
    }

    // AccessToken 로그아웃 여부 검사
    public boolean checkTokenAboutLogout(String token) {
        return redisService.checkExistsValue(token);
    }

    // RefreshToken DB에서 loginname으로 가져오기
    public RefreshToken getTokenDBByLoginname(String loginname) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByKeyLoginname(loginname);
        if (refreshToken.isPresent()) {
            return refreshToken.get();
        } else {
            throw new NullPointerException();
        }
    }



    // 토큰 검증
    public boolean validateToken(String token) throws ExpiredJwtException {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다.");
            throw e;
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // HttpServletRequest 에서 Cookie Value : JWT 가져오기
    public String getTokenFromRequest(String tokenName, HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(tokenName)) {
                    try {
                        return URLDecoder.decode(cookie.getValue(),
                                "UTF-8"); // Encode 되어 넘어간 Value 다시 Decode
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }


}
