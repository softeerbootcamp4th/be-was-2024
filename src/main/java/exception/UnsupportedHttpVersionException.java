package exception;

public class UnsupportedHttpVersionException extends RuntimeException{
    public UnsupportedHttpVersionException() {
        super();
    }

    public UnsupportedHttpVersionException(String message) {
        super(message);
    }

    public UnsupportedHttpVersionException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedHttpVersionException(Throwable cause) {
        super(cause);
    }
}
