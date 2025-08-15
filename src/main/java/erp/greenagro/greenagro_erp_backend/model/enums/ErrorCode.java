package erp.greenagro.greenagro_erp_backend.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {


    private final HttpStatus httpStatus;	// Http 상태 코드
    private final String code;              // 오류코드
    private final String msg;               // 설명ㄴ
}
