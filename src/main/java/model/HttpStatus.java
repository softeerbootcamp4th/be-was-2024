package model;

public enum HttpStatus {

    OK(200, "C001", "success"),
    FOUND(302, "C002", "redirect"),
    NOT_FOUND(404, "C003", "not found")
    ;

    HttpStatus(int status, String code, String message) {
    }
}
