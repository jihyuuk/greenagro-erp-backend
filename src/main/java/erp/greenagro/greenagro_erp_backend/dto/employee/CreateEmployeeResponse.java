package erp.greenagro.greenagro_erp_backend.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateEmployeeResponse {

    private Long employeeId; //직원번호
    private String tempPassword; //임시 비밀번호 (최초 로그인시 변경해야함)

}
