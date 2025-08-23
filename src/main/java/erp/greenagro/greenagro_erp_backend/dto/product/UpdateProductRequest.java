package erp.greenagro.greenagro_erp_backend.dto.product;

import erp.greenagro.greenagro_erp_backend.model.enums.DistChannel;
import erp.greenagro.greenagro_erp_backend.model.enums.TaxType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateProductRequest {

    private String imgUrl;                  //이미지 경로

    private String code;                    //품목코드

    private String name;                    //품목명

    private String spec;                    //규격

    private Long boxQuantity;               //박스 수량

    private Long productGroupId;            //품목 그룹

    private Long partnerId;                //회사

    private TaxType taxType;                //세금 타입(영,과세)

    private DistChannel distChannel;        //유통 채널(계통,비계통)

    private Long purchasePrice;             //매입 단가

    private Long salePrice;                 //출고 단가

    private String memo;                    //비고

    //농약일때만
    private String ingredient;              // 원제
    private String targetPest;              // 적용 해충

}
