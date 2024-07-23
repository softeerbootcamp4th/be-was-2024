package exception;

/**
 * Request와 관련된 예외를 처리하는 클래스
 */
public class RequestException extends RuntimeException{

    private final String message;

    public RequestException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
