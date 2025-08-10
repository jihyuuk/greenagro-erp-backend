package erp.greenagro.greenagro_erp_backend.dto.warehousesite;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateWarehouseSiteRequest {

    private String name;

    private String address;

    private String code;

}
