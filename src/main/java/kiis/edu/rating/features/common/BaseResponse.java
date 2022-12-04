package kiis.edu.rating.features.common;

public class BaseResponse<T> {
    public final int code;
    public final String status, massage, errorClass;
    public final T data;

    public BaseResponse(Status status, String massage, String errorClass, T data) {
        this.code = status.getCode();
        this.status = status.name();
        this.massage = massage;
        this.errorClass = errorClass;
        this.data = data;
    }

    public BaseResponse(Status status, String massage, String errorClass) {
        this(status, massage, errorClass, null);
    }

    public BaseResponse(T data) {
        this(Status.OK, "Success", null, data);
    }
}
