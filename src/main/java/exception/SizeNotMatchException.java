package exception;

public class SizeNotMatchException extends RuntimeException {
    public SizeNotMatchException() {}

    public SizeNotMatchException(String message) {
        super(message);
    }
}
