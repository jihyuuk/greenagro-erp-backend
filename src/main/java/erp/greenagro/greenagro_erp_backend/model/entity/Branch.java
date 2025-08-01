package erp.greenagro.greenagro_erp_backend.model.entity;

import erp.greenagro.greenagro_erp_backend.model.enums.BranchType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Branch {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //지점번호

    private String name; //지점명

    private String ceoName; //대표자

    private String tel; //유선전화

    private String fax; //팩스

    private String email; //이메일

    private String address; //주소

    private String bizNumber; //사업자번호

    private String bizType; //업태

    private String bizItem; //종목

    private LocalDate establishedDate; //개업일자

    private String bizLicenseUrl; //사업자등록증 pdf

    @Enumerated(EnumType.STRING)
    private BranchType branchType; //본점 or 분점

    private String corpRegNumber; //법인등록번호

    public Branch(String name, String ceoName, String tel, String fax, String email, String address, String bizNumber, String bizType, String bizItem, LocalDate establishedDate, String bizLicenseUrl, BranchType branchType, String corpRegNumber) {
        this.name = name;
        this.ceoName = ceoName;
        this.tel = tel;
        this.fax = fax;
        this.email = email;
        this.address = address;
        this.bizNumber = bizNumber;
        this.bizType = bizType;
        this.bizItem = bizItem;
        this.establishedDate = establishedDate;
        this.bizLicenseUrl = bizLicenseUrl;
        this.branchType = branchType;
        this.corpRegNumber = corpRegNumber;
    }
}
