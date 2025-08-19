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
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "A002", "유효하지 않은 토큰입니다."),
    TOKEN_REFRESH_FAILED(HttpStatus.UNAUTHORIZED, "A003", "토큰 재발급에 실패했습니다."),

    // 404 NotFound
    BRANCH_NOT_FOUND(HttpStatus.NOT_FOUND, "N001", "해당 지점을 찾을 수 없습니다."),
    EMPLOYEE_NOT_FOUND(HttpStatus.NOT_FOUND, "N001", "해당 직원을 찾을 수 없습니다."),
    PARTNER_NOT_FOUND(HttpStatus.NOT_FOUND, "N001", "해당 거래처를 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "N001", "해당 품목을 찾을 수 없습니다."),
    PRODUCT_GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "N001", "해당 품목 그룹을 찾을 수 없습니다."),
    WAREHOUSE_SITE_NOT_FOUND(HttpStatus.NOT_FOUND, "N001", "해당 창고 지점을 찾을 수 없습니다."),
    WAREHOUSE_NOT_FOUND(HttpStatus.NOT_FOUND, "N001", "해당 창고를 찾을 수 없습니다."),
    WAREHOUSE_ZONE_FOUND(HttpStatus.NOT_FOUND, "N001", "해당 창고 구역을 찾을 수 없습니다."),

    // 409 Conflict
    DUPLICATE_VALUE(HttpStatus.CONFLICT, "C001", "중복된 데이터가 존재합니다."),
    RESOURCE_IN_USE(HttpStatus.CONFLICT, "C002", "연관된 데이터로 인해 삭제가 불가능합니다."),
    PRODUCT_IN_USE(HttpStatus.CONFLICT, "C002", "연관된 품목으로 인해 삭제가 불가능합니다."),

    // 500 INTERNAL_SERVER_ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S001", "서버 내부 오류가 발생했습니다."),


    ;

    private final HttpStatus httpStatus;	// Http 상태 코드
    private final String code;              // 오류코드
    private final String msg;               // 설명
}
