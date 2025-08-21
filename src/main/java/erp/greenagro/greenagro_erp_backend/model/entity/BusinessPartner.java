package erp.greenagro.greenagro_erp_backend.model.entity;

import erp.greenagro.greenagro_erp_backend.model.enums.SalesGroup;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("BUSINESS")
public class BusinessPartner extends Partner{

    private String bizNo;              // 사업자등록번호 (필수, 유니크 권장)

    private String bizType;            // 업태 (선택)

    private String bizItem;            // 종목 (선택)


    public BusinessPartner(SalesGroup salesGroup, String code, String partnerName, String repName, String tel, String phone, String addressMain, String addressSub, String fax, String email, String ourManager, String partnerManager, String memo, String bizNo, String bizType, String bizItem) {
        super(salesGroup, code, partnerName, repName, tel, phone, addressMain, addressSub, fax, email, ourManager, partnerManager, memo);

        this.bizNo = bizNo;
        this.bizType = bizType;
        this.bizItem = bizItem;
    }

    public void update(SalesGroup salesGroup, String code, String partnerName, String repName, String tel, String phone, String addressMain, String addressSub, String fax, String email, String ourManager, String partnerManager, String memo, String bizNo, String bizType, String bizItem){
            super.setSalesGroup(salesGroup);
            super.setCode(code);
            super.setPartnerName(partnerName);
            super.setRepName(repName);
            super.setTel(tel);
            super.setPhone(phone);
            super.setAddressMain(addressMain);
            super.setAddressSub(addressSub);
            super.setFax(fax);
            super.setEmail(email);
            super.setOurManager(ourManager);
            super.setPartnerManager(partnerManager);
            super.setMemo(memo);

            this.bizNo = bizNo;
            this.bizType = bizType;
            this.bizItem = bizItem;
    }
}
