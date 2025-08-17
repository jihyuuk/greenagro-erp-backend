package erp.greenagro.greenagro_erp_backend.exception;

import erp.greenagro.greenagro_erp_backend.model.enums.ErrorCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final static String ERRORS = "errors";

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ProblemDetail> handleCustom(CustomException exception){

        ErrorCode ec = exception.getErrorCode();

        ProblemDetail pd = ProblemDetail.forStatus(ec.getHttpStatus());
        pd.setType(URI.create("https://에러코드/문서/경로/이거는/임시?code="+ec.getCode()));
        pd.setTitle(ec.getHttpStatus().getReasonPhrase());
        pd.setDetail(ec.getMsg());
        pd.setProperty("code", ec.getCode());
        pd.setProperty("timestamp", java.time.Instant.now());

        //중복 예외
        if(exception instanceof DuplicateValueException dve){
            pd.setProperty(ERRORS, dve.getConflicts());
        }

        //엔티티 존재 x
        if(exception instanceof EntityNotFoundException enfe){
            pd.setProperty(ERRORS, enfe.getErrors());
        }

        return ResponseEntity.status(ec.getHttpStatus()).body(pd);
    }

}
