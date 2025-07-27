package erp.greenagro.greenagro_erp_backend.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class EmployeeSummaryResponse {

    private Long id; //직원번호
    private Long branchId; //지점번호
    private String branchName; //지점명
    private String name; //이름
    private String position; //직위
    private String phone; //전화번호
    private String email; //이메일

}
