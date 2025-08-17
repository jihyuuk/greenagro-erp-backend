package erp.greenagro.greenagro_erp_backend.service;

import erp.greenagro.greenagro_erp_backend.dto.auth.LoginRequest;
import erp.greenagro.greenagro_erp_backend.dto.auth.TokenBundle;
import erp.greenagro.greenagro_erp_backend.exception.CustomException;
import erp.greenagro.greenagro_erp_backend.helper.PasswordHelper;
import erp.greenagro.greenagro_erp_backend.model.entity.Employee;
import erp.greenagro.greenagro_erp_backend.model.enums.AccountStatus;
import erp.greenagro.greenagro_erp_backend.repository.EmployeeRepository;
import erp.greenagro.greenagro_erp_backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static erp.greenagro.greenagro_erp_backend.model.enums.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RefreshTokenRedisService refreshTokenRedisService;
    private final EmployeeRepository employeeRepository;
    private final PasswordHelper passwordHelper;
    private final JwtUtil jwtUtil;


    //아이디 비번으로 로그인
    public TokenBundle login(LoginRequest request){
        //username 으로 직원 조회
        Employee employee = employeeRepository.findByName(request.getUsername()).orElseThrow(() -> new CustomException(INVALID_CREDENTIALS));

        //계정 상태 확인 (ACTIVE 만 로그인 가능)
        if(employee.getStatus() != AccountStatus.ACTIVE){
            throw new CustomException(INVALID_CREDENTIALS);
        }

        //비밀번호 매칭 확인
        if(isNotMatchesPassword(request.getPassword(), employee.getPassword())){
            throw new CustomException(INVALID_CREDENTIALS);
        }

        //기기 식별자 생성 <- 현재는 서버에서 생성하지만 클라에서 생성하는 것도 고려 필요
        String deviceId = UUID.randomUUID().toString();

        //토큰생성
        String accessToken = jwtUtil.generateAccessToken(employee.getId(), employee.getName(), employee.getRole(), deviceId);
        String refreshToken = jwtUtil.generateRefreshToken(employee.getId(), deviceId);

        //refreshToken Redis에 저장
        refreshTokenRedisService.save(employee.getId(), deviceId, refreshToken);

        //반환
        return new TokenBundle(accessToken, refreshToken);
    }


    //accessToken 갱신 (RTR 적용)
    public TokenBundle refresh(String refreshToken){

        //1. 토큰 유효성 검사
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new CustomException(TOKEN_REFRESH_FAILED);
        }

        //2. 직원 id, 기기 식별자 추출
        Long employeeId = jwtUtil.getUserId(refreshToken);
        String deviceId = jwtUtil.getDeviceId(refreshToken);

        //3. refreshToken 유효성 검사 (불일치 및 재사용 했을때)
        if(refreshTokenRedisService.isReused(employeeId, deviceId, refreshToken)){
            //redis 에서 지우기
            refreshTokenRedisService.delete(employeeId, deviceId);
            //예외 던지기
            throw new CustomException(TOKEN_REFRESH_FAILED);
        }

        //4. 직원 DB 조회
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new CustomException(EMPLOYEE_NOT_FOUND, employeeId));

        //4-1.계정 상태 확인 (ACTIVE 만 로그인 가능)
        if(employee.getStatus() != AccountStatus.ACTIVE){
            throw new CustomException(TOKEN_REFRESH_FAILED);
        }

        //5. 토큰 재발급
        String newAccessToken = jwtUtil.generateAccessToken(employeeId, employee.getName(), employee.getRole(), deviceId);
        String newRefreshToken = jwtUtil.generateRefreshToken(employeeId, deviceId);

        //6. 기존 refreshToken 지우고 새로 등록 (RTR)
        refreshTokenRedisService.save(employeeId, deviceId, newRefreshToken);

        return new TokenBundle(newAccessToken, newRefreshToken);
    }


    //로그아웃
    public void logout(String refreshToken){
        //1. 토큰 유효성 검사
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new CustomException(INVALID_TOKEN);
        }

        //2. 클레임 추출
        Long employeeId = jwtUtil.getUserId(refreshToken);
        String deviceId = jwtUtil.getDeviceId(refreshToken);

        //3. Redis 에서 refreshToken 삭제
        refreshTokenRedisService.delete(employeeId, deviceId);

        //accessToken 블랙리스트 고려
    }



    private boolean isNotMatchesPassword(String raw, String encrypted){
        return !passwordHelper.matches(raw, encrypted);
    }

}
