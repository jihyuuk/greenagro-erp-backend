package erp.greenagro.greenagro_erp_backend.dto.warehousesite;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateWarehouseSiteRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String address;

    @NotBlank
    private String code;

}
