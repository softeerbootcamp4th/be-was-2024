package exception;

/**
 * 특정 파일을 찾을 수 없을 경우 발생하는 예외 클래스
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
