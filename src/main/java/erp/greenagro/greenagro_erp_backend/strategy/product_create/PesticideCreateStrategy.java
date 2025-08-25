package erp.greenagro.greenagro_erp_backend.strategy.product_create;

import erp.greenagro.greenagro_erp_backend.dto.product.CreateProductRequest;
import erp.greenagro.greenagro_erp_backend.model.entity.PesticideDetail;
import erp.greenagro.greenagro_erp_backend.model.entity.Product;
import erp.greenagro.greenagro_erp_backend.model.enums.ProductGroupType;
import org.springframework.stereotype.Component;

@Component
public class PesticideCreateStrategy implements ProductDetailCreateStrategy {

    @Override
    public ProductGroupType supports() {
        return ProductGroupType.PESTICIDE;
    }


    @Override
    public void createDetail(Product.ProductBuilder productBuilder, CreateProductRequest request) {

        //1.검증

        //2.디테일 생성
        PesticideDetail pesticideDetail = new PesticideDetail(request.getIngredient(), request.getTargetPest());

        //3.product 빌더에 적용
        productBuilder.pesticideDetail(pesticideDetail);

    }

}
