package me.blogSpringBoot.springbootdeveloper.config.error.exception;
import me.blogSpringBoot.springbootdeveloper.config.error.ErrorCode;

// 비지니스 로직을 작성하다 발생하는 예외
public class BusinessBaseException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessBaseException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessBaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}