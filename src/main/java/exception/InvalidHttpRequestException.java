package exception;

/**
 * HttpRequest에 잘못된 정보가 담겼거나 필요한 정보가 없을 경우 발생하는 예외 클래스
 */
public class InvalidHttpRequestException extends RuntimeException {
    public InvalidHttpRequestException(String message) {
        super(message);
    }
}
