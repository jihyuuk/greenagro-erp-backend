package erp.greenagro.greenagro_erp_backend.repository;

import erp.greenagro.greenagro_erp_backend.model.entity.WarehouseSite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseSiteRepository extends JpaRepository<WarehouseSite, Long> {
    boolean existsByName(String name);

    boolean existsByCode(String code);

}
