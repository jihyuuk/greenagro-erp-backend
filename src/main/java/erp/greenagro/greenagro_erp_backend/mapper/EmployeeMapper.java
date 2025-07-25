package erp.greenagro.greenagro_erp_backend.mapper;

import erp.greenagro.greenagro_erp_backend.dto.employee.CreateEmployeeRequest;
import erp.greenagro.greenagro_erp_backend.dto.employee.CreateEmployeeResponse;
import erp.greenagro.greenagro_erp_backend.model.entity.Branch;
import erp.greenagro.greenagro_erp_backend.model.entity.Employee;
import erp.greenagro.greenagro_erp_backend.model.enums.AccountStatus;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    /**
     * CreateEmployeeRequest ===> Employee 엔티티로 변환합니다.
     */
    public Employee toEntity(CreateEmployeeRequest request, Branch branch, String hashedPwd, String encryptRrn){
        return new Employee(
                branch,                 // 지점
                request.getName(),      // 이름
                hashedPwd,              // 비밀번호
                encryptRrn,             // 주민번호
                request.getPosition(),  // 직위
                request.getPhone(),     // 핸드폰
                request.getEmail(),     // 이메일
                request.getAddress(),   // 주소
                request.getHireDate(),  // 입사일자
                request.getRole(),      // 권한
                AccountStatus.ACTIVE    // 계정상태 활성화
        );
    }
    

    /**
     * Employee 엔티티 ===> CreateEmployeeResponse DTO 로 변환합니다.
     */
    public CreateEmployeeResponse toResponse(Employee employee, String tempPwd){
        return new CreateEmployeeResponse(employee.getId(), tempPwd);
    }

}
