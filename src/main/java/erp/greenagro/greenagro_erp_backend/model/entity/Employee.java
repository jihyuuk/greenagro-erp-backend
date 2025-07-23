package erp.greenagro.greenagro_erp_backend.model.entity;

import erp.greenagro.greenagro_erp_backend.model.enums.AccountStatus;
import erp.greenagro.greenagro_erp_backend.model.enums.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Employee {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //직원번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch; //지점번호

    private String name; //이름

    private String rrn; //주민번호 (뒷 자리 마스킹 필요)

    private String position; //직위

    private String phone; //전화번호

    private String email; //이메일

    private String address; //주소

    private LocalDate hireDate; //입사일자

    private LocalDate retireDate; //퇴사일자

    @Enumerated(EnumType.STRING)
    private Role role; //권한

    @Enumerated(EnumType.STRING)
    private AccountStatus status; // 계정상태

    public Employee(Branch branch, String name, String rrn, String position, String phone, String email, String address, LocalDate hireDate, LocalDate retireDate, Role role, AccountStatus status) {
        this.branch = branch;
        this.name = name;
        this.rrn = rrn;
        this.position = position;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.hireDate = hireDate;
        this.retireDate = retireDate;
        this.role = role;
        this.status = status;
    }
}
