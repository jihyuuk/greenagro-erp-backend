package erp.greenagro.greenagro_erp_backend.repository;

import erp.greenagro.greenagro_erp_backend.model.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
}
