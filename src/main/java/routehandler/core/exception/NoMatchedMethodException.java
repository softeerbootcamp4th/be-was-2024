package routehandler.core.exception;

/**
 * 경로 자체는 있는데 매칭되는 메서드가 없는 경우를 표현하는 예외
 */
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
