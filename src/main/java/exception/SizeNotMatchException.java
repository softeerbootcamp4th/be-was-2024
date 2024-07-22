package exception;

/**
 * Content-Length와 body 크기가 일치하지 않을 경우 발생시키는 런타임 예외
 */
public class SizeNotMatchException extends RuntimeException {
    public SizeNotMatchException() {}

    public SizeNotMatchException(String message) {
        super(message);
    }
}
