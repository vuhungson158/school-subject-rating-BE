package kiis.edu.rating.helper;

import kiis.edu.rating.features.common.BaseResponse;
import kiis.edu.rating.features.common.Status;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static kiis.edu.rating.features.common.Status.*;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public BaseResponse<Object> exceptionHandle(Exception exception) {
        Status status = SERVER_ERROR;
        if (exception instanceof IllegalArgumentException) status = INPUT_INVALID;
        if (exception instanceof InvalidDataAccessApiUsageException) status = DATABASE_MAPPING_ERROR;
        return new BaseResponse<>(status, exception.getMessage(), exception.getClass().getName());
    }
}
