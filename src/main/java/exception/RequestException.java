package exception;

public class RequestException extends RuntimeException{

    private final String message;

    public RequestException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
