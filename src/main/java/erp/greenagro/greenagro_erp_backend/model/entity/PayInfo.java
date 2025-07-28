package erp.greenagro.greenagro_erp_backend.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PayInfo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bankName; // 은행

    private String accountNumber; //계좌번호

    private String depositorName; //예금주

    private Long baseSalary; //기본급

    public PayInfo(String bankName, String accountNumber, String depositorName, Long baseSalary) {
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.depositorName = depositorName;
        this.baseSalary = baseSalary;
    }
}
