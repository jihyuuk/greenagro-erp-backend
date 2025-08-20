package erp.greenagro.greenagro_erp_backend.dto.partner;

import erp.greenagro.greenagro_erp_backend.dto.partner.detail.PartnerDetailBase;
import erp.greenagro.greenagro_erp_backend.model.enums.PartnerType;
import erp.greenagro.greenagro_erp_backend.model.enums.SalesGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PartnerEditResponse {

    private PartnerDetailBase partner; //거래처 상세 정보

    private List<PartnerType> partnerTypeOptions; //사업자 유형 Enum (법인사업자, 개인사업자, 개인)

    private List<SalesGroup> salesGroupOptions; //영업 분류 Enum (조경, 농협, 관공서, 개인, 기타)

}
