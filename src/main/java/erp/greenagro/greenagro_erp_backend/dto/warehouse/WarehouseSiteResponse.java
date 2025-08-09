package erp.greenagro.greenagro_erp_backend.dto.warehouse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WarehouseSiteResponse {

    private Long id;

    private String name;

    private String address;

    private String code;

}
