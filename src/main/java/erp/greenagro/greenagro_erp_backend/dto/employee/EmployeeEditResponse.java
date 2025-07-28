package erp.greenagro.greenagro_erp_backend.dto.employee;

import erp.greenagro.greenagro_erp_backend.dto.branch.BranchSummaryResponse;
import erp.greenagro.greenagro_erp_backend.model.enums.AccountStatus;
import erp.greenagro.greenagro_erp_backend.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class EmployeeEditResponse {

    private EmployeeDetailResponse employee; //기존 직원 상세 정보

    private List<BranchSummaryResponse> branchOptions; //지점 옵션 (id, name)

    private List<Role> roleOptions; //권한 옵션 enum

    private List<AccountStatus> statusOptions; //계정상태 옵션 enum


}
