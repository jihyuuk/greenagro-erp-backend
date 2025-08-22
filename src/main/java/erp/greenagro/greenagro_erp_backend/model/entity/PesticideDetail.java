package erp.greenagro.greenagro_erp_backend.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PesticideDetail {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ingredient;  // 원제

    private String targetPest;  // 적용 해충

    @OneToOne
    @JoinColumn(name = "product_id")
    @Getter(AccessLevel.NONE)
    private Product product;

    public PesticideDetail(String ingredient, String targetPest) {
        this.ingredient = ingredient;
        this.targetPest = targetPest;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void update(String ingredient, String targetPest){
        this.ingredient = ingredient;
        this.targetPest = targetPest;
    }
}
