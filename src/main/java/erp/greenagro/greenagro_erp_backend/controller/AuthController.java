package erp.greenagro.greenagro_erp_backend.controller;

import erp.greenagro.greenagro_erp_backend.dto.auth.LoginRequest;
import erp.greenagro.greenagro_erp_backend.dto.auth.AccessTokenResponse;
import erp.greenagro.greenagro_erp_backend.dto.auth.TokenBundle;
import erp.greenagro.greenagro_erp_backend.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;


@RestController
public class AuthController {

    private final AuthService authService;
    private final Duration REFRESH_TOKEN_EXP; // 7일

    //생성자
    public AuthController(AuthService authService, @Value("${spring.jwt.refresh-exp}") Duration refreshTokenExp) {
        this.authService = authService;
        this.REFRESH_TOKEN_EXP = refreshTokenExp;
    }


    //아이디 비번으로 로그인
    @PostMapping("/auth/login")
    public ResponseEntity<AccessTokenResponse> login(@RequestBody LoginRequest request){
        //access, refresh 토큰 생성
        TokenBundle tokenBundle = authService.login(request);

        //응답 DTO 생성
        AccessTokenResponse response = new AccessTokenResponse(tokenBundle.getAccessToken());

        //refreshToken 쿠키 생성
        ResponseCookie refreshTokenCookie = buildRefreshTokenCookie(tokenBundle.getRefreshToken(), REFRESH_TOKEN_EXP.toSeconds());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(response);
    }


    //accessToken 갱신
    @PostMapping("/auth/refresh")
    public ResponseEntity<AccessTokenResponse> refreshToken(@CookieValue String refreshToken){

        //만약 쿠키 값이 누락되었을 경우 MissingServletRequestCookieException 예외처리 필요함

        //access, refresh 토큰 생성
        TokenBundle tokenBundle = authService.refresh(refreshToken);

        //응답 DTO 생성
        AccessTokenResponse response = new AccessTokenResponse(tokenBundle.getAccessToken());

        //refreshToken 쿠키 생성
        ResponseCookie refreshTokenCookie = buildRefreshTokenCookie(tokenBundle.getRefreshToken(), REFRESH_TOKEN_EXP.toSeconds());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(response);
    }


    //로그아웃
    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout(@CookieValue String refreshToken){

        //만약 쿠키 값이 누락되었을 경우 MissingServletRequestCookieException 예외처리 필요함

        authService.logout(refreshToken);

        ResponseCookie refreshTokenCookie = buildRefreshTokenCookie("", 0L);

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .build();
    }


    private ResponseCookie buildRefreshTokenCookie(String refreshToken, Long maxAge){
        //http 사용
        return ResponseCookie
                .from("refreshToken", refreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge(maxAge)
                .build();

        //https 사용
//        return ResponseCookie
//                .from("refreshToken", refreshToken)
//                .httpOnly(true)
//                .path("/")
//                .maxAge(maAge)
//                .secure(true)
//                .sameSite("None")
//                .build();
    }


}
