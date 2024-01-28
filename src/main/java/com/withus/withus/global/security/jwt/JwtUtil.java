package com.withus.withus.global.security.jwt;

import com.withus.withus.global.exception.BisException;
import com.withus.withus.global.exception.ErrorCode;
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
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class JwtUtil {

    // 사용자 권한 값의 KEY
    private static final String AUTHORIZATION_KEY = "auth";

    private final String REFRESH_TOKEN = "refresh";

    private final String ACCESS_TOKEN = "access";

    // 토큰 만료시간
    private final long ACCESS_TOKEN_TIME = 30 * 60 * 1000L; // 30분
    private final long REFRESH_TOKEN_TIME = 7 * 24 * 60 * 60 * 1000L; // 1주

    @Value("${jwt.secret}") // Base64 Encode 한 SecretKey
    private String secretKey;


    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    private final RedisService redisService;

    public JwtUtil(RedisService redisService) {
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
        cookie.setMaxAge(15 * 60 * 60 * 24); // 쿠키 유효기간 15일

        if (tokenName.equals("refreshToken")) {
            cookie.setHttpOnly(true);
        }

        // Response 객체에 Cookie 추가
        res.addCookie(cookie);
    }

    // Token DB 저장
    public void saveJwtToRedis(String accessToken, String refreshToken, String loginname) {

        redisService.setValues(loginname + ACCESS_TOKEN, accessToken, Duration.ofMinutes(30));
        redisService.setValues(loginname + REFRESH_TOKEN, refreshToken, Duration.ofDays(7));
    }

    // Redis refreshToken 존재여부 검사
    public boolean existTokenInRedis(String token, String tokenName) {
        Claims user = getUserInfoFromToken(token);

        if (tokenName.equals("access")) {
            String accessToken = redisService.getValues(user.getSubject() + ACCESS_TOKEN);
            if(accessToken.isBlank()) {
                return false;
            }
            return true;

        } else if (tokenName.equals("refresh")) {
            String refreshToken = redisService.getValues(user.getSubject() + REFRESH_TOKEN);
            if(refreshToken.isBlank()) {
                return false;
            }
            return true;
        } else {
            throw new BisException(ErrorCode.YOUR_NOT_COME_IN);
        }

    }
    // 중복로그인여부 검증
    public boolean isDuplicateLogin(String accessTokenValue) {
        Claims user = getUserInfoFromToken(accessTokenValue);
        String redisAccessToken = redisService.getValues(user.getSubject() + ACCESS_TOKEN);

        if (accessTokenValue.equals(redisAccessToken)) {
            return false;
        }
        return true;
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


    public boolean existTokenByLoginname(String loginname) {
        String accessToken = redisService.getValues(loginname + ACCESS_TOKEN);
        String refreshToken = redisService.getValues(loginname + REFRESH_TOKEN);
        log.info("엑세스토큰조회 : " + accessToken);
        log.info("리프레시토큰조회 : " + refreshToken);
        if (accessToken.isBlank() && refreshToken.isBlank()) {
            return false;
        } else {
            return true;
        }
    }

    @Transactional
    public String reissuanceAccessToken(String refreshToken) {
        String loginname = getUserInfoFromToken(refreshToken).getSubject();
        String accessToken = createAccessToken(loginname);

        redisService.setValues(loginname + ACCESS_TOKEN, accessToken, Duration.ofMinutes(30));

        return accessToken;
    }

    @Transactional
    public void deleteTokenInRedis(String loginname) {
        redisService.deleteValues(loginname + REFRESH_TOKEN);
        redisService.deleteValues(loginname + ACCESS_TOKEN);
    }

}
