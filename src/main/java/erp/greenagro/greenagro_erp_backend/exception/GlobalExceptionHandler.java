package erp.greenagro.greenagro_erp_backend.exception;

import erp.greenagro.greenagro_erp_backend.model.enums.ErrorCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ProblemDetail> handleCustom(CustomException exception){

        ErrorCode ec = exception.getErrorCode();

        ProblemDetail pd = ProblemDetail.forStatus(ec.getHttpStatus());
        pd.setType(URI.create("https://에러코드/문서/경로/이거는/임시?code="+ec.getCode()));
        pd.setTitle(ec.getHttpStatus().getReasonPhrase());
        pd.setDetail(ec.getMsg());
        pd.setProperty("code", ec.getCode());
        pd.setProperty("timestamp", java.time.Instant.now());

        //데이터 존재시에 property에 추가
        exception.getErrors().ifPresent(errors -> pd.setProperty("errors", errors));


        return ResponseEntity.status(ec.getHttpStatus()).body(pd);
    }

    // 서비스 단에서 중복 검사하더라도
    // 동시성 문제로 인해 DB 유니크 제약조건 위반이 발생할 수 있음
    //@ExceptionHandler(DataIntegrityViolationException.class)

    // 데드락/락획득 실패는 재시도 가능성이 있으니 503/429로 분리 (동시성 대비)
    //@ExceptionHandler({org.springframework.dao.DeadlockLoserDataAccessException.class, org.springframework.dao.CannotAcquireLockException.class})


}
