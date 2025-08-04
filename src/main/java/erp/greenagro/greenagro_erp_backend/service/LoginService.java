package erp.greenagro.greenagro_erp_backend.service;

import erp.greenagro.greenagro_erp_backend.dto.auth.LoginRequest;
import erp.greenagro.greenagro_erp_backend.dto.auth.TokenBundle;
import erp.greenagro.greenagro_erp_backend.helper.PasswordHelper;
import erp.greenagro.greenagro_erp_backend.model.entity.Employee;
import erp.greenagro.greenagro_erp_backend.model.enums.AccountStatus;
import erp.greenagro.greenagro_erp_backend.repository.EmployeeRepository;
import erp.greenagro.greenagro_erp_backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final RefreshTokenRedisService refreshTokenRedisService;
    private final EmployeeRepository employeeRepository;
    private final PasswordHelper passwordHelper;
    private final JwtUtil jwtUtil;


    //아이디 비번으로 로그인
    public TokenBundle login(LoginRequest request){
        //username 으로 직원 조회
        Employee employee = employeeRepository.findByName(request.getUsername()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 직원입니다. 이름: " + request.getUsername()));

        //계정 상태 확인 (ACTIVE 만 로그인 가능)
        if(employee.getStatus() != AccountStatus.ACTIVE){
            throw new IllegalArgumentException("계정상태 로그인 불가");
        }

        //비밀번호 매칭 확인
        if(isNotMatchesPassword(request.getPassword(), employee.getPassword())){
            throw new IllegalArgumentException("비밀번호가 다릅니다.");
        }

        //토큰생성
        String accessToken = jwtUtil.generateAccessToken(employee.getId(), employee.getName(), employee.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(employee.getId());

        //refreshToken redis에 저장
        refreshTokenRedisService.save(employee.getId(), refreshToken);

        //반환
        return new TokenBundle(accessToken, refreshToken);
    }


    //accessToken 갱신 (RTR 적용)
    public TokenBundle refresh(String refreshToken){

        //1. 토큰 유효성 검사
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        //2. id 추출
        Long employeeId = jwtUtil.getUserId(refreshToken);

        //3. refreshToken 유효성 검사 (불일치 및 재사용 했을때)
        if(refreshTokenRedisService.isReused(employeeId, refreshToken)){
            //redis 에서 지우기
            refreshTokenRedisService.delete(employeeId);
            //예외 던지기
            throw new IllegalArgumentException("이미 사용된 refresh token 입니다.");
        }

        //4. 직원 DB 조회
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 직원입니다. id=" + employeeId));

        //4-1.계정 상태 확인 (ACTIVE 만 로그인 가능)
        if(employee.getStatus() != AccountStatus.ACTIVE){
            throw new IllegalArgumentException("계정상태 로그인 불가");
        }

        //5. 토큰 재발급
        String newAccessToken = jwtUtil.generateAccessToken(employeeId, employee.getName(), employee.getRole());
        String newRefreshToken = jwtUtil.generateRefreshToken(employeeId);

        //6. 기존 refreshToken 지우고 새로 등록 (RTR)
        refreshTokenRedisService.save(employeeId, newRefreshToken);

        return new TokenBundle(newAccessToken, newRefreshToken);
    }


    //로그아웃
    public void logout(Long employeeId){
        //redis 에서 refreshToken 삭제
        refreshTokenRedisService.delete(employeeId);

        //accessToken 블랙리스트 고려
    }



    private boolean isNotMatchesPassword(String raw, String encrypted){
        return !passwordHelper.matches(raw, encrypted);
    }

}
