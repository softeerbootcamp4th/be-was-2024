package exception;

/**
 * 잘못된 메소드로 요청
 */
public class BadMethodException extends RuntimeException {
    private static final String statusCode = "405";
    private final String message;

    public BadMethodException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
