package erp.greenagro.greenagro_erp_backend.controller;

import erp.greenagro.greenagro_erp_backend.dto.auth.LoginRequest;
import erp.greenagro.greenagro_erp_backend.dto.auth.AccessTokenResponse;
import erp.greenagro.greenagro_erp_backend.dto.auth.TokenBundle;
import erp.greenagro.greenagro_erp_backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private static final long REFRESH_TOKEN_EXP = 1000 * 60 * 60 * 24 * 7; // 7일 따로 고려


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
    public ResponseEntity<Void> logout(@AuthenticationPrincipal Long userId){
        //스프링 시큐리티에서 principal 가져오기 (나중에 userDetail 사용시 수정 필요)
        authService.logout(userId);

        return ResponseEntity.noContent()
                //https 사용시 .header(HttpHeaders.SET_COOKIE, "\"refreshToken=; HttpOnly; Path=/; Max-Age=0; Secure; SameSite=None")
                .header(HttpHeaders.SET_COOKIE, "refreshToken=; Max-Age=0; Path=/; HttpOnly")
                .build();
    }



    private String buildRefreshTokenCookie(String refreshToken){
        //http 사용
        return String.format("refreshToken=%s; HttpOnly; Path=/; Max-Age=%s", refreshToken, REFRESH_TOKEN_EXP);

        //https 사용
        //return String.format("refreshToken=%s; HttpOnly; Path=/; Max-Age=%s; Secure; SameSite=None", refreshToken, REFRESH_TOKEN_EXP);
    }

}
