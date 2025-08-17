package erp.greenagro.greenagro_erp_backend.exception;

import erp.greenagro.greenagro_erp_backend.dto.exception.LookupDTO;
import erp.greenagro.greenagro_erp_backend.model.enums.ErrorCode;
import lombok.Getter;

import java.util.List;

@Getter
public class ResourceInUseException extends CustomException {

    private List<LookupDTO> errors;

    public ResourceInUseException(ErrorCode errorCode, List<LookupDTO> errors) {
        super(errorCode);
        this.errors = errors;
    }

}