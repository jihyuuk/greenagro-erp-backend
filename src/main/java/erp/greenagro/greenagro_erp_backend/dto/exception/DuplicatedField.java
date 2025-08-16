package erp.greenagro.greenagro_erp_backend.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DuplicatedField {
    private String field;
    private Object value;
}
