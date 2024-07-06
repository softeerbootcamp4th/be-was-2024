package exception;

// HttpRequest 파싱 예외
public class HttpRequestParsingException extends RuntimeException {

    public HttpRequestParsingException(String message) {
        super(message);
    }
}
