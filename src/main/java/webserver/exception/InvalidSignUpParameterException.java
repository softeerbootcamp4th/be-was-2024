package webserver.exception;

import webserver.enums.HttpStatus;

public class InvalidSignUpParameterException extends BaseException {
    public InvalidSignUpParameterException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
