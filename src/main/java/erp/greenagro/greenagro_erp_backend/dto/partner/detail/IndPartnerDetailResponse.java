package erp.greenagro.greenagro_erp_backend.dto.partner.detail;

import erp.greenagro.greenagro_erp_backend.model.enums.PartnerType;
import erp.greenagro.greenagro_erp_backend.model.enums.SalesGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class IndPartnerDetailResponse extends PartnerDetailBase{

    private String rrn;


    public IndPartnerDetailResponse(Long id, @NotBlank String code, @NotNull PartnerType partnerType, @NotNull SalesGroup salesGroup, @NotBlank String partnerName, @NotBlank String repName, String tel, String phone, String addressMain, String addressSub, String fax, String email, String ourManager, String partnerManager, String memo, String rrn) {
        super(id, code, partnerType, salesGroup, partnerName, repName, tel, phone, addressMain, addressSub, fax, email, ourManager, partnerManager, memo);
        this.rrn = rrn;
    }
}
