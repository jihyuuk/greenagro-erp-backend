package erp.greenagro.greenagro_erp_backend.dto.product;

import erp.greenagro.greenagro_erp_backend.dto.partner.PartnerDTO;
import erp.greenagro.greenagro_erp_backend.dto.productgroup.ProductGroupDTO;
import lombok.Getter;

@Getter
public class PesticideSummaryResponse extends ProductSummaryResponse{

    private String ingredient;  // 원제

    private String targetPest;  // 적용 해충

    public PesticideSummaryResponse(Long id, String imgUrl, String code, String name, String spec, Long boxQuantity, ProductGroupDTO productGroup, PartnerDTO partner, String taxType, String distChannel, Long salePrice, String ingredient, String targetPest) {
        super(id, imgUrl, code, name, spec, boxQuantity, productGroup, partner, taxType, distChannel, salePrice);
        this.ingredient = ingredient;
        this.targetPest = targetPest;
    }
}
