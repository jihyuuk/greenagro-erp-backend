package erp.greenagro.greenagro_erp_backend.repository;

import erp.greenagro.greenagro_erp_backend.model.entity.Warehouse;
import erp.greenagro.greenagro_erp_backend.model.entity.WarehouseZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseZoneRepository extends JpaRepository<WarehouseZone, Long> {
    boolean existsByWarehouseAndName(Warehouse warehouse, String name);

    boolean existsByWarehouseAndCode(Warehouse warehouse, String code);

}
