package erp.greenagro.greenagro_erp_backend.dto.employee;

import erp.greenagro.greenagro_erp_backend.dto.payinfo.PayInfoDTO;
import erp.greenagro.greenagro_erp_backend.model.enums.AccountStatus;
import erp.greenagro.greenagro_erp_backend.model.enums.Role;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class UpdateEmployeeRequest {

    @NotNull
    private Long branchId; //지점번호

    @NotBlank
    private String name; //이름

    private String rrn; //주민등록번호

    private String position; //직위

    private String phone; //전화번호

    @Email
    private String email; //이메일

    private String address; //주소

    @NotNull
    private LocalDate hireDate; //입사일자

    @NotNull
    private LocalDate resignDate; //퇴사일자

    @NotNull
    private Role role; //권한

    @NotNull
    private AccountStatus status; //계정상태

    @Valid
    private PayInfoDTO payInfo; //급여정보(은행,계좌,예금주,기본급)

}
