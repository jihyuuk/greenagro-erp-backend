package erp.greenagro.greenagro_erp_backend.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LookupDTO {

    private final Long id;
    private final String code;
    private final String name;

}
