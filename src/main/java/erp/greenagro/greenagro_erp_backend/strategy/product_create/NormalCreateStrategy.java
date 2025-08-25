package erp.greenagro.greenagro_erp_backend.strategy.product_create;

import erp.greenagro.greenagro_erp_backend.dto.product.CreateProductRequest;
import erp.greenagro.greenagro_erp_backend.model.entity.Product;
import erp.greenagro.greenagro_erp_backend.model.enums.ProductGroupType;
import org.springframework.stereotype.Component;

@Component
public class NormalCreateStrategy  implements ProductDetailCreateStrategy{

    @Override
    public ProductGroupType supports() {
        return ProductGroupType.NORMAL;
    }


    @Override
    public void createDetail(Product.ProductBuilder productBuilder, CreateProductRequest request) {
        // 따로 디테일 없어서 할게 없음 ㅋㅋㅋ
    }

}
