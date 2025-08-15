package erp.greenagro.greenagro_erp_backend.model.entity;

import erp.greenagro.greenagro_erp_backend.model.enums.CustomerType;
import erp.greenagro.greenagro_erp_backend.model.enums.SalesGroup;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;

@Getter
@Entity
@SoftDelete
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CustomerType customerType; //사업자 유형 (법인사업자, 개인사업자, 개인)

    @Enumerated(EnumType.STRING)
    private SalesGroup salesGroup;    //영업 분류 (조경, 농협, 관공서, 개인, 기타)

    private String corpNo;      //법인번호 (법인사업자만)

    private String bizNo;       //사업자번호 (개인 X)

    private String rrn;         //주민번호 (개인만)

    private String bizName;     //상호 (개인 X)

    private String ceoName;     //대표자명

    private String bizType;     //업태 (개인 X)

    private String bizItem;     //종목 (개인 X)

    private String tel;         //전화

    private String phone;       //휴대폰

    private String addressMain; //주소1 (사업장 주소)

    private String addressSub;  //주소2 (배송 주소)

    private String fax;         //팩스

    private String email;       //이메일

    private String ourManager;  //우리 담당자 (우리 회사 직원)

    private String customerManager; //거래처 담당자 (고객 회사 직원)

    private String memo;        //비고


    public Customer(CustomerType customerType, SalesGroup salesGroup, String corpNo, String bizNo, String rrn, String bizName, String ceoName, String bizType, String bizItem, String tel, String phone, String addressMain, String addressSub, String fax, String email, String ourManager, String customerManager, String memo) {
        this.customerType = customerType;
        this.salesGroup = salesGroup;
        this.corpNo = corpNo;
        this.bizNo = bizNo;
        this.rrn = rrn;
        this.bizName = bizName;
        this.ceoName = ceoName;
        this.bizType = bizType;
        this.bizItem = bizItem;
        this.tel = tel;
        this.phone = phone;
        this.addressMain = addressMain;
        this.addressSub = addressSub;
        this.fax = fax;
        this.email = email;
        this.ourManager = ourManager;
        this.customerManager = customerManager;
        this.memo = memo;
    }


    //업데이트
    public void update(CustomerType customerType, SalesGroup salesGroup, String corpNo, String bizNo, String rrn, String bizName, String ceoName, String bizType, String bizItem, String tel, String phone, String addressMain, String addressSub, String fax, String email, String ourManager, String customerManager, String memo) {
        this.customerType = customerType;
        this.salesGroup = salesGroup;
        this.corpNo = corpNo;
        this.bizNo = bizNo;
        this.rrn = rrn;
        this.bizName = bizName;
        this.ceoName = ceoName;
        this.bizType = bizType;
        this.bizItem = bizItem;
        this.tel = tel;
        this.phone = phone;
        this.addressMain = addressMain;
        this.addressSub = addressSub;
        this.fax = fax;
        this.email = email;
        this.ourManager = ourManager;
        this.customerManager = customerManager;
        this.memo = memo;
    }

}
