package erp.greenagro.greenagro_erp_backend.dto.branch;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class BranchSummaryResponse {

    private Long id;    // 지점번호
    private String name;// 지점명

}
