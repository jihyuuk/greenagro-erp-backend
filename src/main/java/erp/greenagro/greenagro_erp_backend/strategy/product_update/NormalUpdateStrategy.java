package erp.greenagro.greenagro_erp_backend.strategy.product_update;

import erp.greenagro.greenagro_erp_backend.dto.product.UpdateProductRequest;
import erp.greenagro.greenagro_erp_backend.model.entity.Product;
import erp.greenagro.greenagro_erp_backend.model.entity.ProductGroup;
import erp.greenagro.greenagro_erp_backend.model.enums.ProductGroupType;
import org.springframework.stereotype.Component;

@Component
public class NormalUpdateStrategy implements ProductDetailUpdateStrategy{

    // ? -> 일반 으로 그룹변경 할때

    @Override
    public ProductGroupType supports() {
        return ProductGroupType.NORMAL;
    }


    @Override
    public void updateDetail(Product product, ProductGroup originGroup, ProductGroup updateGroup, UpdateProductRequest request) {

        //기존 그룹이 일반이 아니었을때
        if(!originGroup.equals(updateGroup)){
            // 모든 디테일 초기화
            product.resetDetails();
        }

    }

}
