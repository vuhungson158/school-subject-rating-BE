package kiis.edu.rating.helper;

import kiis.edu.rating.features.common.BaseResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static kiis.edu.rating.features.common.Status.BAD_REQUEST;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public BaseResponse<Object> exceptionHandle(Exception exception) {
        System.out.println("Caught Exception");
        return new BaseResponse<>(BAD_REQUEST, exception.getMessage(), exception.getClass().getName());
    }
}
