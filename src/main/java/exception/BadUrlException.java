package exception;

/**
 * 잘못된 URL로 요청
 */
public class BadUrlException extends RuntimeException {
    private static final String statusCode = "405";
    private final String message;

    public BadUrlException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
