package erp.greenagro.greenagro_erp_backend.dto.warehousezone;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateWarehouseZoneRequest {

    private Long warehouseId;

    private String name;

    private String code;

}
