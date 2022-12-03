package kiis.edu.rating.features.common;

public class BaseResponse<T> {
    private final int code;
    private final String status, massage;
    private final T data;

    public BaseResponse(Status status, String massage, T data) {
        this.code = status.getCode();
        this.status = status.name();
        this.massage = massage;
        this.data = data;
    }

    public BaseResponse(String massage) {
        this(Status.BAD_REQUEST, massage, null);
    }

    public BaseResponse(T data) {
        this(Status.OK, "Success", data);
    }
}
