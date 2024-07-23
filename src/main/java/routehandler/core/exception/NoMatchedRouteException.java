package routehandler.core.exception;

/**
 * 대응되는 경로가 없음을 표현하는 예외
 */
public class NoMatchedRouteException extends RuntimeException {

    public NoMatchedRouteException() {
        super();
    }

    public NoMatchedRouteException(String message) {
        super(message);
    }

    public NoMatchedRouteException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoMatchedRouteException(Throwable cause) {
        super(cause);
    }

    protected NoMatchedRouteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
