package erp.greenagro.greenagro_erp_backend.repository;

import erp.greenagro.greenagro_erp_backend.model.entity.Warehouse;
import erp.greenagro.greenagro_erp_backend.model.entity.WarehouseSite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    boolean existsByWarehouseSiteAndName(WarehouseSite warehouseSite, String name);

    boolean existsByWarehouseSiteAndCode(WarehouseSite warehouseSite, String code);

}
