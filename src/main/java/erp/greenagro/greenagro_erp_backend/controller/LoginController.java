package erp.greenagro.greenagro_erp_backend.controller;

import erp.greenagro.greenagro_erp_backend.dto.login.LoginRequest;
import erp.greenagro.greenagro_erp_backend.dto.login.LoginResponse;
import erp.greenagro.greenagro_erp_backend.dto.login.TokenBundle;
import erp.greenagro.greenagro_erp_backend.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private static final long REFRESH_TOKEN_EXP = 1000 * 60 * 60 * 24 * 7; // 7일 따로 고려

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
        //access, refresh 토큰 생성
        TokenBundle tokenBundle = loginService.login(request);

        //응답 DTO 생성
        LoginResponse response = new LoginResponse(tokenBundle.getAccessToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, buildRefreshTokenCookie(tokenBundle.getRefreshToken()))
                .body(response);
    }


    private String buildRefreshTokenCookie(String refreshToken){
        //http 사용
        return String.format("refreshToken=%s; HttpOnly; Path=/; Max-Age=%s", refreshToken, REFRESH_TOKEN_EXP);

        //https 사용
        //return String.format("refreshToken=%s; HttpOnly; Path=/; Max-Age=%s; Secure; SameSite=None", refreshToken, REFRESH_TOKEN_EXP);
    }

}
