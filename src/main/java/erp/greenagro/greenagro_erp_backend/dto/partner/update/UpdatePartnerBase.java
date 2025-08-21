package erp.greenagro.greenagro_erp_backend.dto.partner.update;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import erp.greenagro.greenagro_erp_backend.model.enums.SalesGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "partnerType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = UpdateBizPartnerRequest.class, name = "BUSINESS"),
        @JsonSubTypes.Type(value = UpdateIndPartnerRequest.class, name = "INDIVIDUAL")
})
public abstract class UpdatePartnerBase {

    @NotBlank
    private String code;        //거래처 코드

    //private PartnerType partnerType; //사업자 유형 (법인사업자, 개인사업자, 개인) - 요청시 필수 포함

    @NotNull
    private SalesGroup salesGroup;   //영업 분류 (조경, 농협, 관공서, 개인, 기타)

    @NotBlank
    private String partnerName; //거래처명

    @NotBlank
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

}
