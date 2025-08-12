package erp.greenagro.greenagro_erp_backend.repository;

import erp.greenagro.greenagro_erp_backend.model.entity.ProductGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductGroupRepository extends JpaRepository<ProductGroup, Long> {
    boolean existsByNameAndDeletedFalse(String name);

    List<ProductGroup> findAllByDeletedFalse();

    Optional<ProductGroup> findByIdAndDeletedFalse(Long id);

}
