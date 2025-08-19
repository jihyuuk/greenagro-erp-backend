package erp.greenagro.greenagro_erp_backend.dto.partner;

import erp.greenagro.greenagro_erp_backend.model.enums.PartnerType;
import erp.greenagro.greenagro_erp_backend.model.enums.SalesGroup;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreatePartnerRequest {

    @NotNull
    private PartnerType partnerType; //사업자 유형 (법인사업자, 개인사업자, 개인)

    @NotNull
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

    private String partnerManager; //거래처 담당자 (거래처 회사 직원)

    private String memo;        //비고

}
