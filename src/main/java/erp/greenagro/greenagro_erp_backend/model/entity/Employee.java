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

    private String password; //비밀번호 (BCrypt 암호화)

    private String rrn; //주민번호 (뒷 자리 마스킹 필요)

    private String position; //직위

    private String phone; //전화번호

    private String email; //이메일

    private String address; //주소

    private LocalDate hireDate; //입사일자

    private LocalDate resignDate; //퇴사일자

    @Enumerated(EnumType.STRING)
    private Role role; //권한

    @Enumerated(EnumType.STRING)
    private AccountStatus status; // 계정상태

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "pay_info_id")
    private PayInfo payInfo; // 급여정보

    private Boolean mustChangePassword; // 비밀번호 변경 필요 여부

    //신규 직원 등록용 생성자
    public Employee(Branch branch, String name, String password, String rrn, String position, String phone, String email, String address, LocalDate hireDate, Role role, AccountStatus status, PayInfo payInfo) {
        this.branch = branch;
        this.name = name;
        this.password = password;
        this.rrn = rrn;
        this.position = position;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.hireDate = hireDate;
        this.role = role;
        this.status = status;
        this.payInfo = payInfo;
        this.mustChangePassword = true;
    }


    //직원 업데이트
    //UpdateEmployeeCommand 고려해봐야함 파라미터가 너무 많음
    public void update(Branch branch, String name, String rrn, String position, String phone, String email, String address, LocalDate hireDate, LocalDate resignDate, Role role, AccountStatus status) {
        this.branch = branch;
        this.name = name;
        this.rrn = rrn;
        this.position = position;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.hireDate = hireDate;
        this.resignDate = resignDate;
        this.role = role;
        this.status = status;
    }


    //직원 퇴사처리
    public void resign(LocalDate resignDate){
        //계정 상태 변경
        this.status = AccountStatus.RESIGNED;
        //퇴사일 적용
        this.resignDate = resignDate == null ? LocalDate.now() : resignDate;//null 방지
    }

    //직원 비밀번호 초기화
    public void resetPassword(String hashedPwd) {
        this.password = hashedPwd;
        this.mustChangePassword = true;
    }
}
