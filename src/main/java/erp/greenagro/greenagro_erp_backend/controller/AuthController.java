package erp.greenagro.greenagro_erp_backend.controller;

import erp.greenagro.greenagro_erp_backend.dto.auth.LoginRequest;
import erp.greenagro.greenagro_erp_backend.dto.auth.AccessTokenResponse;
import erp.greenagro.greenagro_erp_backend.dto.auth.TokenBundle;
import erp.greenagro.greenagro_erp_backend.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;


@RestController
public class AuthController {

    private final AuthService authService;
    private final Duration REFRESH_TOKEN_EXP; // 7일 따로 고려

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

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, buildRefreshTokenCookie(tokenBundle.getRefreshToken()))
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

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, buildRefreshTokenCookie(tokenBundle.getRefreshToken()))
                .body(response);
    }


    //로그아웃
    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout(@CookieValue String refreshToken){

        //만약 쿠키 값이 누락되었을 경우 MissingServletRequestCookieException 예외처리 필요함

        authService.logout(refreshToken);

        return ResponseEntity.noContent()
                //https 사용시 .header(HttpHeaders.SET_COOKIE, "\"refreshToken=; HttpOnly; Path=/; Max-Age=0; Secure; SameSite=None")
                .header(HttpHeaders.SET_COOKIE, "refreshToken=; Max-Age=0; Path=/; HttpOnly")
                .build();
    }



    private String buildRefreshTokenCookie(String refreshToken){
        //http 사용
        return String.format("refreshToken=%s; HttpOnly; Path=/; Max-Age=%s", refreshToken, REFRESH_TOKEN_EXP.toSeconds());

        //https 사용
        //return String.format("refreshToken=%s; HttpOnly; Path=/; Max-Age=%s; Secure; SameSite=None", refreshToken, REFRESH_TOKEN_EXP.toSeconds());
    }

}
