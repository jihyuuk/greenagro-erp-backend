package erp.greenagro.greenagro_erp_backend.util;


import erp.greenagro.greenagro_erp_backend.model.enums.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private final SecretKey SECRET_KEY;
    private final Duration ACCESS_TOKEN_EXP; //유효기간 - 30분
    private final Duration REFRESH_TOKEN_EXP; //유효기간 - 7일

    public JwtUtil(
            @Value("${spring.jwt.secret}") String secret,
            @Value("${spring.jwt.access-exp}") Duration accessTokenExp,
            @Value("${spring.jwt.refresh-exp}") Duration refreshTokenExp
    ){
        this.SECRET_KEY = Keys.hmacShaKeyFor(secret.getBytes());
        this.ACCESS_TOKEN_EXP = accessTokenExp;
        this.REFRESH_TOKEN_EXP = refreshTokenExp;
    }

    /**
     * access-token 생성
     */
    public String generateAccessToken(Long userId, String userName, Role role){
        return Jwts.builder()
                .setSubject("access-token")
                .claim("userId", userId)
                .claim("userName", userName)
                .claim("role", role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXP.toMillis()))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }


    /**
     * refresh-token 생성
     */
    public String generateRefreshToken(Long userId){
        return Jwts.builder()
                .setSubject("refresh-token")
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXP.toMillis()))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }


    /**
     * JWT 토큰 검증
     */
    public boolean validateToken(String token){
        
        try {
            getClaims(token); //여기서 유효성검사 자동 실행된다.
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("JWT 만료됨");
        } catch (JwtException e) {
            log.warn("JWT 유효하지 않음");
        }

        return false;
    }


    /**
     * JWT 토큰 만료 여부 확인
     */
    public boolean isExpired(String token){
        try {
            return getClaims(token).getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (JwtException e) {
            //위조된 토큰 → 일단 만료된 걸로 간주
            log.warn("유효하지 않은 토큰입니다: {}", e.getMessage());
            return true;
        }
    }


    /**
     * 직원 id 가져오기
     */
    public Long getUserId(String token){
        return getClaims(token).get("userId", Long.class);
    }


    /**
     * 직원 이름 가져오기
     */
    public String getUserName(String token){
        return getClaims(token).get("userName", String.class);
    }


    /**
     * 직원 권한 가져오기
     */
    public Role getRole(String token){
        String strRole = getClaims(token).get("role", String.class);
        return Role.valueOf(strRole);
    }


    //모든 claims 가져오기
    public Claims getClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
