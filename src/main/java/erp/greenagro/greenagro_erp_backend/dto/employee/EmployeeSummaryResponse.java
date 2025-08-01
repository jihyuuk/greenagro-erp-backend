package erp.greenagro.greenagro_erp_backend.dto.employee;

import erp.greenagro.greenagro_erp_backend.dto.branch.BranchSummaryResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class EmployeeSummaryResponse {

    private Long id; //직원번호
    private BranchSummaryResponse branch; // 지점 (지점번호, 지점명)
    private String name; //이름
    private String position; //직위
    private String phone; //전화번호
    private String email; //이메일

}
