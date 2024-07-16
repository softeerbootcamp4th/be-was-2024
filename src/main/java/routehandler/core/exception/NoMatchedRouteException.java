package routehandler.core.exception;

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
