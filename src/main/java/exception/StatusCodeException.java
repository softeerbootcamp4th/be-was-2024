package exception;

import type.StatusCodeType;

public class StatusCodeException extends Exception {
    StatusCodeType statusCode;
    public StatusCodeException(StatusCodeType sc) {
        this.statusCode = sc;
    }

    public StatusCodeType getStatusCode() { return statusCode; }
}
