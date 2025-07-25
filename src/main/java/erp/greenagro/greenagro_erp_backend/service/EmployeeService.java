package erp.greenagro.greenagro_erp_backend.service;

import erp.greenagro.greenagro_erp_backend.dto.employee.CreateEmployeeRequest;
import erp.greenagro.greenagro_erp_backend.dto.employee.CreateEmployeeResponse;
import erp.greenagro.greenagro_erp_backend.mapper.EmployeeMapper;
import erp.greenagro.greenagro_erp_backend.model.entity.Branch;
import erp.greenagro.greenagro_erp_backend.model.entity.Employee;
import erp.greenagro.greenagro_erp_backend.model.enums.AccountStatus;
import erp.greenagro.greenagro_erp_backend.repository.BranchRepository;
import erp.greenagro.greenagro_erp_backend.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final BranchRepository branchRepository;
    private final EmployeeMapper employeeMapper;

    private static final String AES_KEY = "0123456789abcdef";

    //직원 등록
    @Transactional
    public CreateEmployeeResponse createEmployee(CreateEmployeeRequest request) {
        //지점 조회하기
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new IllegalArgumentException("지점이 존재하지 않습니다."));

        //임시 비밀번호 생성
        String tempPwd = UUID.randomUUID().toString().substring(0, 4); //4자리 랜덤 비밀번호
        //비밀번호 해시화
        String hashedPwd = new BCryptPasswordEncoder().encode(tempPwd);

        //주민번호 암호화
        String encryptRrn = "";
        try {
            SecretKeySpec key = new SecretKeySpec(AES_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(request.getRrn().getBytes());
            encryptRrn = Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("주민번호 암호화 실패", e);
        }

        //employee 객체 생성
        Employee employee = employeeMapper.toEntity(request, branch, hashedPwd, encryptRrn);

        //저장
        employeeRepository.save(employee);

        //response 반환
        return employeeMapper.toResponse(employee, tempPwd);
    }


}
