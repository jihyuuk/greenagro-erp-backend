package erp.greenagro.greenagro_erp_backend.strategy;

import erp.greenagro.greenagro_erp_backend.dto.product.UpdateProductRequest;
import erp.greenagro.greenagro_erp_backend.model.entity.PesticideDetail;
import erp.greenagro.greenagro_erp_backend.model.entity.Product;
import erp.greenagro.greenagro_erp_backend.model.entity.ProductGroup;
import org.springframework.stereotype.Component;

@Component("PESTICIDE")
public class PesticideUpdateStrategy implements ProductDetailUpdateStrategy {

    // ? -> 농약 으로 그룹변경 할때

    @Override
    public void updateDetails(Product product, ProductGroup originGroup, ProductGroup updateGroup, UpdateProductRequest request) {

        System.out.println("전략: 농약 선택됌");

        //기존 그룹이 농약이 아니었을때
        if(!originGroup.equals(updateGroup)){
            System.out.println("기존 기룹이 농약이 아니었을때 로직 실행");
            // 모든 디테일 초기화
            product.resetDetails();

            // 농약 디테일 생성
            PesticideDetail pesticideDetail = new PesticideDetail(request.getIngredient(), request.getTargetPest());
            product.setPesticideDetail(pesticideDetail);
        }else{
            System.out.println("농약 -> 농약 로직 실행");
            //농약 -> 농약 (그룹 변경 x)

            //농약 디테일 수정
            PesticideDetail pesticideDetail = product.getPesticideDetail();
            pesticideDetail.update(request.getIngredient(), request.getTargetPest());
        }

    }

}
