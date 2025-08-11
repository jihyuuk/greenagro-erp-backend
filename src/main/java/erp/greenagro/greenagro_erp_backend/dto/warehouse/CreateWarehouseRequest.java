package erp.greenagro.greenagro_erp_backend.dto.warehouse;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateWarehouseRequest {

    @NotNull
    private Long warehouseSiteId;

    @NotBlank
    private String name;

    @NotBlank
    private String code;

}
