package erp.greenagro.greenagro_erp_backend.dto.payinfo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PayInfoDTO {

    private String bankName; // 은행

    private String accountNumber; //계좌번호

    private String depositorName; //예금주

    private Long baseSalary; //기본급
}
