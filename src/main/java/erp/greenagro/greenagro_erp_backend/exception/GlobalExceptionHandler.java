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

}
