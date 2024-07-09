package url.exception;

public class IllegalUrlException extends RuntimeException {
    public IllegalUrlException() {
        super();
    }

    public IllegalUrlException(String message) {
        super(message);
    }
    public IllegalUrlException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalUrlException(Throwable cause) {
        super(cause);
    }

    protected IllegalUrlException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
