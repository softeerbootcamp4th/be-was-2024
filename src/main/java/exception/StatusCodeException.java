package exception;

import type.HTTPStatusCode;

public class StatusCodeException extends Exception {
    HTTPStatusCode statusCode;
    public StatusCodeException(HTTPStatusCode sc) {
        this.statusCode = sc;
    }

    public HTTPStatusCode getStatusCode() { return statusCode; }
}
