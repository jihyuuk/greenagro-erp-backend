package erp.greenagro.greenagro_erp_backend.util;


import erp.greenagro.greenagro_erp_backend.model.enums.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION = 1000 * 60 * 30; //유효기간 - 30분

    /**
     * JWT 토큰 생성
     */
    public String generateToken(Long userId, String userName, Role role){
        return Jwts.builder()
                .setSubject("access-token")
                .claim("userId", userId)
                .claim("userName", userName)
                .claim("role", role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
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
        return getClaims(token).getExpiration().before(new Date());
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
    private Claims getClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
