package erp.greenagro.greenagro_erp_backend.dto.warehousezone;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateWarehouseZoneRequest {

    @NotNull
    private Long warehouseId;

    @NotBlank
    private String name;

    @NotBlank
    private String code;

}
