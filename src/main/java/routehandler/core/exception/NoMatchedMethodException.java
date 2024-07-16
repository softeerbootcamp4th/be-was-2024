package routehandler.core.exception;

public class NoMatchedMethodException extends RuntimeException {
    public NoMatchedMethodException() {
        super();
    }

    public NoMatchedMethodException(String message) {
        super(message);
    }

    public NoMatchedMethodException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoMatchedMethodException(Throwable cause) {
        super(cause);
    }

    protected NoMatchedMethodException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
