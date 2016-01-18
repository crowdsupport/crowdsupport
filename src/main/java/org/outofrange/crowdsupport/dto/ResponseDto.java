package org.outofrange.crowdsupport.dto;

public class ResponseDto<T> {
    private ResponseType type;
    private String message;
    private T data;

    protected ResponseDto() {

    }

    public static ResponseDto<Void> success(String message) {
        return new ResponseDto<Void>().setType(ResponseType.SUCCESS).setMessage(message);
    }

    public static ResponseDto<Void> error(String message) {
        return new ResponseDto<Void>().setType(ResponseType.ERROR).setMessage(message);
    }

    public static ResponseDto<Void> info(String message) {
        return new ResponseDto<Void>().setType(ResponseType.INFO).setMessage(message);
    }

    public static <T> ResponseDto<T> success(String message, T data) {
        return new ResponseDto<T>().setType(ResponseType.SUCCESS).setMessage(message).setData(data);
    }

    public static <T> ResponseDto<T> error(String message, T data) {
        return new ResponseDto<T>().setType(ResponseType.ERROR).setMessage(message).setData(data);
    }

    public static <T> ResponseDto<T> info(String message, T data) {
        return new ResponseDto<T>().setType(ResponseType.INFO).setMessage(message).setData(data);
    }

    public ResponseType getType() {
        return type;
    }

    public ResponseDto<T> setType(ResponseType type) {
        this.type = type;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ResponseDto<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public ResponseDto<T> setData(T data) {
        this.data = data;
        return this;
    }
}
