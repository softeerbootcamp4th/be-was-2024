package exception;

/**
 * 405 예와를 나타내는 예외 클래스
 */
public class MethodNotAllowedException extends RuntimeException {
    public MethodNotAllowedException(String message) {
        super(message);
    }
}
