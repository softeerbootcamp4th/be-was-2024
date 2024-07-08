package exception;

public class InvalidHttpRequestException extends RuntimeException {
    // 기본 생성자
    public InvalidHttpRequestException() {
        super();
    }

    // 메시지를 받는 생성자
    public InvalidHttpRequestException(String message) {
        super(message);
    }

    // 메시지와 원인(cause)을 받는 생성자
    public InvalidHttpRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    // 원인(cause)을 받는 생성자
    public InvalidHttpRequestException(Throwable cause) {
        super(cause);
    }
}