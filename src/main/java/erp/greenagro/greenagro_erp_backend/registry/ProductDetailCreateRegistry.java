package erp.greenagro.greenagro_erp_backend.registry;

import erp.greenagro.greenagro_erp_backend.exception.CustomException;
import erp.greenagro.greenagro_erp_backend.model.enums.ErrorCode;
import erp.greenagro.greenagro_erp_backend.model.enums.ProductGroupType;
import erp.greenagro.greenagro_erp_backend.strategy.product_create.ProductDetailCreateStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProductDetailCreateRegistry {

    private final Map<ProductGroupType, ProductDetailCreateStrategy> strategyMap;


    public ProductDetailCreateRegistry(List<ProductDetailCreateStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(ProductDetailCreateStrategy::supports, s -> s));
    }


    public ProductDetailCreateStrategy get(ProductGroupType type) {
        return Optional.ofNullable(strategyMap.get(type))
                .orElseThrow(() -> new CustomException(ErrorCode.INTERNAL_SERVER_ERROR));
    }

}
