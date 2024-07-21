package exception;

/**
 * DB 관련 예외 클래스
 */
public class DatabaseException extends RuntimeException {
    public DatabaseException(String message) {
        super(message);
    }
}
