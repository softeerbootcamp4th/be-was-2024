package http;

public enum HttpStatus {

    OK(200, "Ok"),
    FOUND(302, "Found"),
    BAD_REQUEST(400, "Bad request"),
    NOT_FOUND(404, "Not found"),
    UNAUTHORIZED(401, "Unauthorized"),
    CONFLICT(409, "Conflict");

    private int status;
    private String message;

    HttpStatus(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
