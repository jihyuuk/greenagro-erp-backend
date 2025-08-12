package erp.greenagro.greenagro_erp_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductGroupRepository extends JpaRepository<ProductGroupRepository, Long> {
}
