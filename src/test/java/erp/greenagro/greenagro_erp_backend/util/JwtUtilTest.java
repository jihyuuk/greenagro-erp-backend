package erp.greenagro.greenagro_erp_backend.util;

import erp.greenagro.greenagro_erp_backend.model.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtUtilTest {

    @Autowired
    JwtUtil jwtUtil;
    @Value("${spring.jwt.secret}")
    String SECRET_KEY;
    @Value("${spring.jwt.access-exp}")
    Duration ACCESS_TOKEN_EXP;
    @Value("${spring.jwt.refresh-exp}")
    Duration REFRESH_TOKEN_EXP;


    @Test
    void 엑세스_토큰_생성() {
        //given
        Long userId = 1L;
        String userName = "홍길동";
        Role role = Role.ADMIN;

        //when
        String accessToken = jwtUtil.generateAccessToken(userId, userName, role);

        //then
        assertNotNull(accessToken); //토큰 존재 여부
        assertTrue(jwtUtil.validateToken(accessToken)); //유효성 검증
        assertEquals(jwtUtil.getClaims(accessToken).getSubject(), "access-token"); //subject 검증
        assertEquals(jwtUtil.getUserId(accessToken), userId); //claims 값 검증
        assertEquals(jwtUtil.getUserName(accessToken), userName);
        assertEquals(jwtUtil.getRole(accessToken), role);
    }


    @Test
    void 리프레시_토큰_생성() {
        //given
        Long userId = 3L;

        //when
        String refreshToken = jwtUtil.generateRefreshToken(userId);

        //then
        assertNotNull(refreshToken); //토큰 존재 여부
        assertTrue(jwtUtil.validateToken(refreshToken)); //유효성 검증
        assertFalse(jwtUtil.isExpired(refreshToken)); //만료 여부
        assertEquals(jwtUtil.getClaims(refreshToken).getSubject(), "refresh-token"); //subject 검증
        assertEquals(jwtUtil.getUserId(refreshToken), userId); //claims 값 검증
    }

    @Test
    void 토큰_유효성_검사() {
        SecretKey secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        SecretKey fakeKey = Keys.hmacShaKeyFor("이건가짜시크릿키지롱하하하하하하하하하하하하".getBytes());

        //given
        String normalToken = generateTestToken(new Date(), new Date(System.currentTimeMillis() + 100000L), secretKey); //정상 토큰
        String fakeToken = generateTestToken(new Date(), new Date(System.currentTimeMillis() + 100000L), fakeKey); //서명 위조 토큰
        String expiredToken = generateTestToken(new Date(System.currentTimeMillis() - 10000L), new Date(System.currentTimeMillis() - 5000L), secretKey); //만료된 토큰

        //then
        assertTrue(jwtUtil.validateToken(normalToken));
        assertFalse(jwtUtil.validateToken(fakeToken));
        assertFalse(jwtUtil.validateToken(expiredToken));

        assertFalse(jwtUtil.isExpired(normalToken));
        assertTrue(jwtUtil.isExpired(expiredToken));
    }


    @Test
    void 토큰_유효기간_확인() {
        //given
        String accessToken = jwtUtil.generateAccessToken(1L, "홍길동", Role.ADMIN);
        String refreshToken = jwtUtil.generateRefreshToken(3L);

        //claims 추출
        Claims accessClaims = jwtUtil.getClaims(accessToken);
        Claims refreshClaims = jwtUtil.getClaims(refreshToken);

        //엑세스 토큰 시작일, 만료일 추출
        Date accessIat = accessClaims.getIssuedAt();
        Date accessExp = accessClaims.getExpiration();

        //리프레시 토큰 시작일, 만료일 추출
        Date refreshIat = refreshClaims.getIssuedAt();
        Date refreshExp = refreshClaims.getExpiration();

        //유효기간 계산
        long actualAccessMinutes = Duration.ofMillis(accessExp.getTime() - accessIat.getTime()).toMinutes();
        long actualRefreshDays = Duration.ofMillis(refreshExp.getTime() - refreshIat.getTime()).toDays();


        //then
        assertEquals(ACCESS_TOKEN_EXP.toMinutes(), actualAccessMinutes);
        assertEquals(REFRESH_TOKEN_EXP.toDays(), actualRefreshDays);
    }




    private String generateTestToken(Date iat, Date exp, SecretKey key) {
        return Jwts.builder()
                .setSubject("test-token")
                .claim("userId", 1L)
                .setIssuedAt(iat)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

}