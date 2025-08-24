package erp.greenagro.greenagro_erp_backend.strategy.product_create;

import erp.greenagro.greenagro_erp_backend.dto.product.CreateProductRequest;
import erp.greenagro.greenagro_erp_backend.model.entity.Product;

public interface ProductDetailCreateStrategy {

    /**
     * 품목 생성시 타입별 전략
     * @param request 품목 생성 요청 DTO
     * @return 품목(Product)
     */
    void createDetail(Product.ProductBuilder productBuilder, CreateProductRequest request);

}