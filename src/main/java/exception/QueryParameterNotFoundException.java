package exception;

public class QueryParameterNotFoundException extends RuntimeException {
    // 기본 생성자
    public QueryParameterNotFoundException() {
        super();
    }

    // 메시지를 받는 생성자
    public QueryParameterNotFoundException(String message) {
        super(message);
    }

    // 메시지와 원인(cause)을 받는 생성자
    public QueryParameterNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    // 원인(cause)을 받는 생성자
    public QueryParameterNotFoundException(Throwable cause) {
        super(cause);
    }
}
