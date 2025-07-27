package erp.greenagro.greenagro_erp_backend.dto.employee;

import erp.greenagro.greenagro_erp_backend.model.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class UpdateEmployeeRequest {

    private Long branchId; //지점번호
    private String name; //이름
    private String rrn; //주민번호
    private String position; //직위
    private String phone; //전화번호
    private String email; //이메일
    private String address; //주소
    private LocalDate hireDate; //입사일자

    @Enumerated(EnumType.STRING)
    private Role role; //권한

}
