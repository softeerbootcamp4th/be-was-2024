package webserver.exception;

import webserver.enums.HttpStatus;

public class BaseException extends RuntimeException {

    private final HttpStatus httpStatus;

    public BaseException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

}