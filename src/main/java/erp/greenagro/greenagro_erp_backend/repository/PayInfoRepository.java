package erp.greenagro.greenagro_erp_backend.repository;

import erp.greenagro.greenagro_erp_backend.model.entity.PayInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayInfoRepository extends JpaRepository<PayInfo, Long> {
}
