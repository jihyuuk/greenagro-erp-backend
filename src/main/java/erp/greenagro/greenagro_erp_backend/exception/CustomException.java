package erp.greenagro.greenagro_erp_backend.exception;

import erp.greenagro.greenagro_erp_backend.model.enums.ErrorCode;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;

@Getter
public class CustomException extends RuntimeException{

    private final ErrorCode errorCode;      // 에러 코드 (필수)
    private final Optional<Object> errors;  // 에러 관련 데이터 (선택)

    //에러 코드만 (기본)
    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.errorCode = errorCode;
        this.errors = Optional.empty();
    }

    //에러 코드 + ID - notFound 사용
    public CustomException(ErrorCode errorCode, Long id){
        super(errorCode.getMsg());
        this.errorCode = errorCode;
        this.errors = Optional.of(Map.of("id", id));
    }


    //에러 코드 + errors
    public CustomException(ErrorCode errorCode, Object errors) {
        super(errorCode.getMsg());
        this.errorCode = errorCode;
        this.errors = Optional.of(errors);
    }
}
