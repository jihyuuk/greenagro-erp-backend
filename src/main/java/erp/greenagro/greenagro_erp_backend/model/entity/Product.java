package erp.greenagro.greenagro_erp_backend.model.entity;

import erp.greenagro.greenagro_erp_backend.model.enums.DistChannel;
import erp.greenagro.greenagro_erp_backend.model.enums.TaxType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imgUrl;                  //이미지 경로

    private String code;                    //품목코드

    private String name;                    //품목명

    private String spec;                    //규격

    private Long boxQuantity;               //박스 수량

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "product_group_id")
    private ProductGroup productGroup;      //품목 그룹

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;              //회사

    @Enumerated(EnumType.STRING)
    private TaxType taxType;                //세금 타입(영,과세)

    @Enumerated(EnumType.STRING)
    private DistChannel distChannel;        //유통 채널(계통,비계통)

    private Long purchasePrice;             //매입 단가

    private Long salePrice;                 //출고 단가

    private String memo;                    //비고

}
