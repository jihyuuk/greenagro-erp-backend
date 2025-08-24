package erp.greenagro.greenagro_erp_backend.dto.product;

import erp.greenagro.greenagro_erp_backend.dto.partner.PartnerDTO;
import erp.greenagro.greenagro_erp_backend.dto.productgroup.ProductGroupDTO;
import erp.greenagro.greenagro_erp_backend.model.enums.DistChannel;
import erp.greenagro.greenagro_erp_backend.model.enums.TaxType;
import lombok.Getter;

@Getter
public class PesticideDetailResponse extends ProductDetailResponse{

    private String ingredient;  // 원제

    private String targetPest;  // 적용 해충


    public PesticideDetailResponse(Long id, String imgUrl, String code, String name, String spec, Long boxQuantity, ProductGroupDTO productGroup, PartnerDTO partner, TaxType taxType, DistChannel distChannel, Long purchasePrice, Long salePrice, String memo, String ingredient, String targetPest) {
        super(id, imgUrl, code, name, spec, boxQuantity, productGroup, partner, taxType, distChannel, purchasePrice, salePrice, memo);
        this.ingredient = ingredient;
        this.targetPest = targetPest;
    }
}
