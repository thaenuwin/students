package test.students;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import test.students.utils.ResponseMessageUtil;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import java.util.Map;

@ControllerAdvice
public class ExceptionTranslator {

    @ExceptionHandler({PersistenceException.class, ConstraintViolationException.class, NullPointerException.class})
    public ResponseEntity<Object> handleAccessDeniedException(
            Exception ex, WebRequest request) {
        String ERRCLASS = "error_class";
        String ERRDESC = "error_description";
        Map<String, Object> map = ResponseMessageUtil.createFailResponse();
        map.put(ERRCLASS, "SQLException");
        map.put(ERRDESC, ex.getMessage());
        ex.printStackTrace();
        return new ResponseEntity<Object>(
                map, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
