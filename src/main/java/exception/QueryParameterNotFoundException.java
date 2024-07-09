package exception;

public class QueryParameterNotFoundException extends RuntimeException {
    public QueryParameterNotFoundException() {
        super();
    }

    public QueryParameterNotFoundException(String message) {
        super(message);
    }

    public QueryParameterNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public QueryParameterNotFoundException(Throwable cause) {
        super(cause);
    }
}
