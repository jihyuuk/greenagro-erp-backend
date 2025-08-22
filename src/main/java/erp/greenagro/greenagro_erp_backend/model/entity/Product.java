package erp.greenagro.greenagro_erp_backend.model.entity;

import erp.greenagro.greenagro_erp_backend.exception.CustomException;
import erp.greenagro.greenagro_erp_backend.model.enums.DistChannel;
import erp.greenagro.greenagro_erp_backend.model.enums.ErrorCode;
import erp.greenagro.greenagro_erp_backend.model.enums.ProductGroupType;
import erp.greenagro.greenagro_erp_backend.model.enums.TaxType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;

import static erp.greenagro.greenagro_erp_backend.model.enums.ProductGroupType.*;

@Getter
@Entity
@SoftDelete
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imgUrl;                  //이미지 경로

    private String code;                    //품목코드

    private String name;                    //품목명

    private String spec;                    //규격

    private Long boxQuantity;               //박스 수량

    @ManyToOne
    @JoinColumn(name = "product_group_id")
    private ProductGroup productGroup;      //품목 그룹

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;                //회사

    @Enumerated(EnumType.STRING)
    private TaxType taxType;                //세금 타입(영,과세)

    @Enumerated(EnumType.STRING)
    private DistChannel distChannel;        //유통 채널(계통,비계통)

    private Long purchasePrice;             //매입 단가

    private Long salePrice;                 //출고 단가

    private String memo;                    //비고

    //추가 필드가있는 그룹 -------------

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private PesticideDetail pesticideDetail; //농약 추가필드


    @Builder
    public Product(String imgUrl, String code, String name, String spec, Long boxQuantity, ProductGroup productGroup, Partner partner, TaxType taxType, DistChannel distChannel, Long purchasePrice, Long salePrice, String memo, PesticideDetail pesticideDetail) {
        this.imgUrl = imgUrl;
        this.code = code;
        this.name = name;
        this.spec = spec;
        this.boxQuantity = boxQuantity;
        this.productGroup = productGroup;
        this.partner = partner;
        this.taxType = taxType;
        this.distChannel = distChannel;
        this.purchasePrice = purchasePrice;
        this.salePrice = salePrice;
        this.memo = memo;

        //검증 필요
        if(productGroup.getType() == PESTICIDE){
            setPesticideDetail(pesticideDetail);
        }

    }


    //수정하기
    public void update(String imgUrl, String code, String name, String spec, Long boxQuantity, ProductGroup productGroup, Partner partner, TaxType taxType, DistChannel distChannel, Long purchasePrice, Long salePrice, String memo) {
        this.imgUrl = imgUrl;
        this.code = code;
        this.name = name;
        this.spec = spec;
        this.boxQuantity = boxQuantity;
        this.productGroup = productGroup;
        this.partner = partner;
        this.taxType = taxType;
        this.distChannel = distChannel;
        this.purchasePrice = purchasePrice;
        this.salePrice = salePrice;
        this.memo = memo;
    }


    //양방향 설정
    public void setPesticideDetail(PesticideDetail pesticideDetail) {
        this.pesticideDetail = pesticideDetail;

        if(pesticideDetail != null){
            pesticideDetail.setProduct(this); //이거 안하면 pesticideDetail 에 productId null 들어옴
        }
    }

    public void resetDetails(){
        this.pesticideDetail = null;
    }
}
