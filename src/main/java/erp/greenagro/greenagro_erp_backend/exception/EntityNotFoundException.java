package erp.greenagro.greenagro_erp_backend.exception;

import erp.greenagro.greenagro_erp_backend.model.enums.ErrorCode;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class EntityNotFoundException extends CustomException{

    private Map<String, Long> errors = new LinkedHashMap<>();

    public EntityNotFoundException(ErrorCode errorCode, Long id) {

        super(errorCode);

        errors.put("id", id);

    }

}