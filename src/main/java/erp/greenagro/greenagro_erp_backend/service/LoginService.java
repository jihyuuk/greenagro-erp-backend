package erp.greenagro.greenagro_erp_backend.service;

import erp.greenagro.greenagro_erp_backend.dto.login.LoginRequest;
import erp.greenagro.greenagro_erp_backend.dto.login.LoginResponse;
import erp.greenagro.greenagro_erp_backend.helper.PasswordHelper;
import erp.greenagro.greenagro_erp_backend.model.entity.Employee;
import erp.greenagro.greenagro_erp_backend.repository.EmployeeRepository;
import erp.greenagro.greenagro_erp_backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final EmployeeRepository employeeRepository;
    private final PasswordHelper passwordHelper;
    private final JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest request){
        //username 으로 직원 조회
        Employee employee = employeeRepository.findByName(request.getUsername()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 직원입니다. 이름: " + request.getUsername()));

        //비밀번호 매칭 확인
        if(isNotMatchesPassword(request.getPassword(), employee.getPassword())){
            throw new IllegalArgumentException("비밀번호가 다릅니다.");
        }

        //토큰생성
        String jwtToken = jwtUtil.generateToken(employee.getId(), employee.getName(), employee.getRole());

        //반환
        return new LoginResponse(jwtToken);
    }


    private boolean isNotMatchesPassword(String raw, String encrypted){
        return !passwordHelper.matches(raw, encrypted);
    }

}
