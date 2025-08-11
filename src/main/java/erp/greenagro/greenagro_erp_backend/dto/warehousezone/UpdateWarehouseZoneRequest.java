package erp.greenagro.greenagro_erp_backend.dto.warehousezone;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateWarehouseZoneRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String code;

}
