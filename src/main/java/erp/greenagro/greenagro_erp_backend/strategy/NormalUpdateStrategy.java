package erp.greenagro.greenagro_erp_backend.strategy;

import erp.greenagro.greenagro_erp_backend.dto.product.UpdateProductRequest;
import erp.greenagro.greenagro_erp_backend.model.entity.Product;
import erp.greenagro.greenagro_erp_backend.model.entity.ProductGroup;
import org.springframework.stereotype.Component;

@Component("NORMAL")
public class NormalUpdateStrategy implements ProductDetailUpdateStrategy{

    // ? -> 일반 으로 그룹변경 할때

    @Override
    public void updateDetails(Product product, ProductGroup originGroup, ProductGroup updateGroup, UpdateProductRequest request) {

        System.out.println("전략: 일반 선택 됨");

        //기존 그룹이 일반이 아니었을때
        if(!originGroup.equals(updateGroup)){
            System.out.println("기존 기룹이 일반이 아니었을때 로직 실행");
            // 모든 디테일 초기화
            product.resetDetails();
        }

    }

}
