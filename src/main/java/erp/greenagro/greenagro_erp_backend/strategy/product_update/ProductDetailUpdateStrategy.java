package erp.greenagro.greenagro_erp_backend.strategy.product_update;

import erp.greenagro.greenagro_erp_backend.dto.product.UpdateProductRequest;
import erp.greenagro.greenagro_erp_backend.model.entity.Product;
import erp.greenagro.greenagro_erp_backend.model.entity.ProductGroup;
import erp.greenagro.greenagro_erp_backend.model.enums.ProductGroupType;

public interface ProductDetailUpdateStrategy {

    //지원 타입 반환
    ProductGroupType supports();


    /**
     * 품목 그룹 수정에 의한 추가 필드 변경 및 수정 전략
     * @param product 업데이트할 품목
     * @param originGroup 기존 그룹
     * @param updateGroup 변경할 그룹
     * @param request 업데이트 DTO
     */
    void updateDetail(Product product, ProductGroup originGroup, ProductGroup updateGroup, UpdateProductRequest request);

}
