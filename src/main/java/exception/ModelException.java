package exception;

public class ModelException extends RuntimeException{

    private final String message;

    public ModelException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
