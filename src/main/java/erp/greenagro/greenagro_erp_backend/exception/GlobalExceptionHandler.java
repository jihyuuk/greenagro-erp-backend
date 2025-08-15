package erp.greenagro.greenagro_erp_backend.exception;

import erp.greenagro.greenagro_erp_backend.model.enums.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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

        if( exception instanceof DuplicateValueException dve){
            pd.setProperty("conflicts", dve.getConflicts());
        }

        return ResponseEntity.status(ec.getHttpStatus()).body(pd);
    }

}
