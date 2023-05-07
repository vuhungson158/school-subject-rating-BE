package kiis.edu.rating.helper;

import kiis.edu.rating.features.common.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@SuppressWarnings("unused")
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public BaseResponse<Object> exceptionHandle(Exception exception) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = exception.getMessage();
        if (exception instanceof IllegalArgumentException) {
            status = HttpStatus.BAD_REQUEST;
        }
        if (exception instanceof AccessDeniedException) {
            status = HttpStatus.FORBIDDEN;
            message = "Your Role doesn't have permission to call this API";
        }
        if (exception instanceof BadCredentialsException) {
            status = HttpStatus.UNAUTHORIZED;
        }
        return new BaseResponse<>(status, message, exception.getClass().getName());
    }
}
