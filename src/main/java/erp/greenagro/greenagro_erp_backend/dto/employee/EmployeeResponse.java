package erp.greenagro.greenagro_erp_backend.dto.employee;

import erp.greenagro.greenagro_erp_backend.model.entity.Branch;
import erp.greenagro.greenagro_erp_backend.model.enums.AccountStatus;
import erp.greenagro.greenagro_erp_backend.model.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class EmployeeResponse {

    private Long id; //직원번호
    private Long branchId; //지점번호
    private String branchName; //지점명
    private String name; //이름
    private String position; //직위
    private String phone; //전화번호
    private String email; //이메일
    private LocalDate hireDate; //입사일자
    private LocalDate resignDate; //퇴사일자
    private Role role; //권한
    private AccountStatus accountStatus; //계정상태

}
