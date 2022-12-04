package kiis.edu.rating.helper;

import kiis.edu.rating.features.common.BaseResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static kiis.edu.rating.features.common.Status.BAD_REQUEST;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public BaseResponse<Object> exceptionHandle(Exception exception) {
        return new BaseResponse<>(BAD_REQUEST, exception.getMessage(), exception.getClass().getName());
    }
}
