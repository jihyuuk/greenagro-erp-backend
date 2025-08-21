package erp.greenagro.greenagro_erp_backend.model.entity;

import erp.greenagro.greenagro_erp_backend.model.enums.PartnerType;
import erp.greenagro.greenagro_erp_backend.model.enums.SalesGroup;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;

@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@SoftDelete
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "partner_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Partner {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Enumerated(EnumType.STRING)
//    private PartnerType partnerType; //사업자 유형 (법인사업자, 개인사업자, 개인)

    @Enumerated(EnumType.STRING)
    private SalesGroup salesGroup;   //영업 분류 (조경, 농협, 관공서, 개인, 기타)

    private String code;        //거래처 코드

    private String partnerName; //거래처명

    private String repName;     //대표자

    private String tel;         //전화

    private String phone;       //휴대폰

    private String addressMain; //주소1 (사업장 주소)

    private String addressSub;  //주소2 (배송 주소)

    private String fax;         //팩스

    private String email;       //이메일

    private String ourManager;  //우리 담당자 (우리 회사 직원)

    private String partnerManager; //거래처 담당자 (거래처 회사 직원)

    private String memo;        //비고


    public Partner(SalesGroup salesGroup, String code, String partnerName, String repName, String tel, String phone, String addressMain, String addressSub, String fax, String email, String ourManager, String partnerManager, String memo) {
        this.salesGroup = salesGroup;
        this.code = code;
        this.partnerName = partnerName;
        this.repName = repName;
        this.tel = tel;
        this.phone = phone;
        this.addressMain = addressMain;
        this.addressSub = addressSub;
        this.fax = fax;
        this.email = email;
        this.ourManager = ourManager;
        this.partnerManager = partnerManager;
        this.memo = memo;
    }
}
