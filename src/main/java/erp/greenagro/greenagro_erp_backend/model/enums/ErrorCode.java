package erp.greenagro.greenagro_erp_backend.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 400 Bad Request
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "B001", "유효하지 않은 입력값입니다."),

    // 401 Unauthorized
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "A001", "직원명 또는 비밀번호가 올바르지 않습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "A001", "유효하지 않은 토큰입니다."),
    TOKEN_REFRESH_FAILED(HttpStatus.UNAUTHORIZED, "A002", "토큰 재발급에 실패했습니다."),

    // 409 Conflict
    DUPLICATE_VALUE(HttpStatus.CONFLICT, "C001", "중복된 데이터가 존재합니다."),
    RESOURCE_IN_USE(HttpStatus.CONFLICT, "C002", "연관된 데이터로 인해 삭제가 불가능합니다."),

    // 500 INTERNAL_SERVER_ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S001", "서버 내부 오류가 발생했습니다."),


    ;

    private final HttpStatus httpStatus;	// Http 상태 코드
    private final String code;              // 오류코드
    private final String msg;               // 설명
}
