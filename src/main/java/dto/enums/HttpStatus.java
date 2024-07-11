package dto.enums;

public enum HttpStatus {

    OK(200, "OK"),
    CREATED(201, "Created"),
    ACCEPTED(202, "Accepted"),
    NO_CONTENT(204, "No Content"),
    SEE_OTHER(303, "See Other"),
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    NOT_IMPLEMENTED(501, "Not Implemented");


    private final int httpStatusCode;
    private final String httpStatusMessage;

    HttpStatus(int httpStatusCode, String httpStatusMessage){
        this.httpStatusCode=httpStatusCode;
        this.httpStatusMessage=httpStatusMessage;
    }


    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getHttpStatusMessage() {
        return httpStatusMessage;
    }
}
