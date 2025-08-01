package erp.greenagro.greenagro_erp_backend.mapper;

import erp.greenagro.greenagro_erp_backend.dto.branch.BranchSummaryResponse;
import erp.greenagro.greenagro_erp_backend.dto.employee.*;
import erp.greenagro.greenagro_erp_backend.dto.payinfo.PayInfoDTO;
import erp.greenagro.greenagro_erp_backend.model.entity.Branch;
import erp.greenagro.greenagro_erp_backend.model.entity.Employee;
import erp.greenagro.greenagro_erp_backend.model.entity.PayInfo;
import erp.greenagro.greenagro_erp_backend.model.enums.AccountStatus;
import erp.greenagro.greenagro_erp_backend.model.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EmployeeMapper {


    /**
     * CreateEmployeeRequest ===> Employee 엔티티로 변환합니다.
     */
    public Employee fromCreate(CreateEmployeeRequest request, Branch branch, PayInfo payInfo, String hashedPwd, String encryptedRrn) {
        return new Employee(
                branch,                 // 지점
                request.getName(),      // 이름
                hashedPwd,              // 비밀번호
                encryptedRrn,           // 주민번호
                request.getPosition(),  // 직위
                request.getPhone(),     // 핸드폰
                request.getEmail(),     // 이메일
                request.getAddress(),   // 주소
                request.getHireDate(),  // 입사일자
                request.getRole(),      // 권한
                AccountStatus.ACTIVE,   // 계정상태 활성화
                payInfo                 // 급여정보
        );
    }


    /**
     * Employee 엔티티 ===> CreateEmployeeResponse DTO 로 변환합니다.
     */
    public CreateEmployeeResponse toCreate(Employee employee, String tempPwd) {
        return new CreateEmployeeResponse(employee.getId(), tempPwd);
    }

    public EmployeeSummaryResponse toSummary(Employee employee, BranchSummaryResponse branchSummaryResponse) {

        return new EmployeeSummaryResponse(
                employee.getId(),               //직원번호
                branchSummaryResponse,          // 지점(지점번호, 지점명)
                employee.getName(),             // 이름
                employee.getPosition(),         // 직위
                employee.getPhone(),            // 핸드폰
                employee.getEmail()             // 이메일
        );
    }

    public EmployeeDetailResponse toDetail(Employee employee, String decryptRrn, BranchSummaryResponse branchSummaryResponse, PayInfoDTO payInfoDTO){

        return new EmployeeDetailResponse(
                employee.getId(),               //직원번호
                branchSummaryResponse,          //지점(지점번호, 지점명)
                employee.getName(),             //이름
                decryptRrn,                     //주민등록번호
                employee.getPosition(),         //직위
                employee.getPhone(),            //핸드폰
                employee.getEmail(),            //이메일
                employee.getAddress(),          //주소
                employee.getHireDate(),         //입사일
                employee.getResignDate(),       //퇴사일
                employee.getRole(),             //권한
                employee.getStatus(),           //계정상태
                payInfoDTO                      //급여정보(은행,계좌,예금주,기본급)
        );
    }

    public EmployeeEditResponse toEdit(EmployeeDetailResponse employeeDetail, List<BranchSummaryResponse> branchSummaryResponses, List<Role> roles, List<AccountStatus> accountStatuses) {
        return new EmployeeEditResponse(
                employeeDetail,
                branchSummaryResponses,
                roles,
                accountStatuses
        );
    }
}
