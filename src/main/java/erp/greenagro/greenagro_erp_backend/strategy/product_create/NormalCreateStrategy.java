package erp.greenagro.greenagro_erp_backend.strategy.product_create;

import erp.greenagro.greenagro_erp_backend.dto.product.CreateProductRequest;
import erp.greenagro.greenagro_erp_backend.model.entity.Product;
import org.springframework.stereotype.Component;

@Component("NORMAL_CREATE")
public class NormalCreateStrategy  implements ProductDetailCreateStrategy{

    @Override
    public void createDetail(Product.ProductBuilder productBuilder, CreateProductRequest request) {
        // 따로 디테일 없어서 할게 없음 ㅋㅋㅋ
    }

}
