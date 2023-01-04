package kiis.edu.rating.features.common;

import org.springframework.http.HttpStatus;

public class BaseResponse<T> {
    public final int code;
        public final String status, massage, errorClass;
    public final T data;

    public BaseResponse(HttpStatus status, String massage, String errorClass, T data) {
        this.code = status.value();
        this.status = status.name();
        this.massage = massage;
        this.errorClass = errorClass;
        this.data = data;
    }

    public BaseResponse(HttpStatus status, String massage, String errorClass) {
        this(status, massage, errorClass, null);
    }

    public BaseResponse(T data) {
        this(HttpStatus.OK, "Success", null, data);
    }
}
