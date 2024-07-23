package exception;

/**
 * Model과 관련된 예외를 처리하는 클래스
 */
public class ModelException extends RuntimeException{

    private final String message;

    public ModelException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
