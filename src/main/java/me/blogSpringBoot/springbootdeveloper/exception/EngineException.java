package me.blogSpringBoot.springbootdeveloper.exception;

/**
 * 예외 처리 클래스
 */
public class EngineException  extends Exception {

    private int status = 200;

    public EngineException() {}

    public EngineException(int status) {
        this.status = status;
    }

    /**
     * @brief 생성자 Admin Controller Exception Handler
     *
     * @param cause 에러 예외 객체
     */
    public EngineException(Throwable cause) {
        super(cause);
    }

    /**
     * @brief 생성자  Admin Controller Exception Handler
     *
     * @param message 에러 메세지
     * @param cause 에러 예외 객체
     */
    public EngineException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @brief 생성자 Admin Controller Exception Handler
     *
     * @param code 에러 코드
     * @param message 에러 메세지
     */
    public EngineException(int code, String message) {
        super(message);
        this.status = code;
    }

    /**
     * @brief 생성자  Admin Controller Exception Handler
     *
     * @param code 에러 코드
     * @param message 에러 메세지
     * @param cause 에러 예외 객체
     */
    public EngineException(int code, String message, Throwable cause) {
        super(message, cause);
        this.status = code;
    }

}