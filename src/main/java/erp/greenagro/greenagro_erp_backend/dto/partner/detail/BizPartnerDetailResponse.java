package erp.greenagro.greenagro_erp_backend.dto.partner.detail;

import erp.greenagro.greenagro_erp_backend.model.enums.PartnerType;
import erp.greenagro.greenagro_erp_backend.model.enums.SalesGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class BizPartnerDetailResponse extends PartnerDetailBase {

    private String bizNo;              // 사업자등록번호 (필수, 유니크 권장)

    private String bizType;            // 업태 (선택)

    private String bizItem;            // 종목 (선택)


    public BizPartnerDetailResponse(Long id, @NotBlank String code, @NotNull PartnerType partnerType, @NotNull SalesGroup salesGroup, @NotBlank String partnerName, @NotBlank String repName, String tel, String phone, String addressMain, String addressSub, String fax, String email, String ourManager, String partnerManager, String memo, String bizNo, String bizType, String bizItem) {
        super(id, code, partnerType, salesGroup, partnerName, repName, tel, phone, addressMain, addressSub, fax, email, ourManager, partnerManager, memo);
        this.bizNo = bizNo;
        this.bizType = bizType;
        this.bizItem = bizItem;
    }
}
