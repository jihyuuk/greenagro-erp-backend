package erp.greenagro.greenagro_erp_backend.dto.warehouse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateWarehouseRequest {

    private Long warehouseSiteId;

    private String name;

    private String code;

}
