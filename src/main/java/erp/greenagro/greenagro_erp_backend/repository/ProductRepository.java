package erp.greenagro.greenagro_erp_backend.repository;

import erp.greenagro.greenagro_erp_backend.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
