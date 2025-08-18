package erp.greenagro.greenagro_erp_backend.repository;

import erp.greenagro.greenagro_erp_backend.model.entity.Product;
import erp.greenagro.greenagro_erp_backend.model.entity.ProductGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByProductGroup(ProductGroup productGroup);

    boolean existsByCode(String code);

    boolean existsByName(String name);

    List<Product> findAllByProductGroup(ProductGroup productGroup);
}
