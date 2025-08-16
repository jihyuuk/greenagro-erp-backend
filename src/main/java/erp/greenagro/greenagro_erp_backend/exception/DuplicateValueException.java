package erp.greenagro.greenagro_erp_backend.exception;

import erp.greenagro.greenagro_erp_backend.dto.exception.DuplicatedField;
import erp.greenagro.greenagro_erp_backend.model.enums.ErrorCode;
import lombok.Getter;

import java.util.List;

@Getter
public class DuplicateValueException extends CustomException{

    private List<DuplicatedField> conflicts; //해당 필드, 중복된 값

    public DuplicateValueException(List<DuplicatedField> conflicts) {
        super(ErrorCode.DUPLICATE_VALUE);
        this.conflicts = conflicts;
    }

}
