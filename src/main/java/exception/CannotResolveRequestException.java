package exception;

/**
 * HTTP Method 파싱이 불가능할 경우 발생하는 런타임 예외
 */
public class CannotResolveRequestException extends RuntimeException {
    public CannotResolveRequestException() {}

    public CannotResolveRequestException(String message) {
        super(message);
    }
}
