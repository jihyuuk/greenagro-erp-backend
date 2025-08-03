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

@Service
@RequiredArgsConstructor
public class LoginService {

    private final RefreshTokenRedisService refreshTokenRedisService;
    private final EmployeeRepository employeeRepository;
    private final PasswordHelper passwordHelper;
    private final JwtUtil jwtUtil;

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


    private boolean isNotMatchesPassword(String raw, String encrypted){
        return !passwordHelper.matches(raw, encrypted);
    }

}
