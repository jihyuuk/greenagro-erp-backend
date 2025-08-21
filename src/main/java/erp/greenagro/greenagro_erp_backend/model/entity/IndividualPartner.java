package erp.greenagro.greenagro_erp_backend.model.entity;

import erp.greenagro.greenagro_erp_backend.model.enums.PartnerType;
import erp.greenagro.greenagro_erp_backend.model.enums.SalesGroup;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("INDIVIDUAL")
public class IndividualPartner extends Partner{

    private String rrnEncrypted;   // 주민등록번호 (AES 암호화 저장)

    //private String birthYmd;       // 생년월일


    public IndividualPartner(SalesGroup salesGroup, String code, String partnerName, String repName, String tel, String phone, String addressMain, String addressSub, String fax, String email, String ourManager, String partnerManager, String memo, String rrnEncrypted) {
        super(salesGroup, code, partnerName, repName, tel, phone, addressMain, addressSub, fax, email, ourManager, partnerManager, memo);

        this.rrnEncrypted = rrnEncrypted;
    }


    public void update(SalesGroup salesGroup, String code, String partnerName, String repName, String tel, String phone, String addressMain, String addressSub, String fax, String email, String ourManager, String partnerManager, String memo, String rrnEncrypted){
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

        this.rrnEncrypted = rrnEncrypted;
    }
}
