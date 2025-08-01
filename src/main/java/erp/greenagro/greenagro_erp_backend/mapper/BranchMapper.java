package erp.greenagro.greenagro_erp_backend.mapper;

import erp.greenagro.greenagro_erp_backend.dto.branch.BranchSummaryResponse;
import erp.greenagro.greenagro_erp_backend.model.entity.Branch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class BranchMapper {

    public BranchSummaryResponse toResponse(Branch branch){
        return new BranchSummaryResponse(
                branch.getId(),  //지점번호
                branch.getName() //지점명
        );
    }

}
