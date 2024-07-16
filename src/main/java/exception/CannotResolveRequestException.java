package exception;

public class CannotResolveRequestException extends RuntimeException {
    public CannotResolveRequestException() {}

    public CannotResolveRequestException(String message) {
        super(message);
    }
}
