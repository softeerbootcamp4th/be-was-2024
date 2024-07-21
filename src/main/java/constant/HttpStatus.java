package constant;

/**
 * HttpStatus Enum
 */
public enum HttpStatus {
    OK(200, "OK"),
    CREATED(201, "CREATED"),
    NO_CONTENT(204, "NO CONTENT"),
    FOUND(302, "FOUND"),
    BAD_REQUEST(400, "BAD REQUEST"),
    NOT_FOUND(404, "NOT FOUND"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL SERVICE ERROR");

    final int statusCode;
    final String message;

    HttpStatus(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
