package util;

public enum HttpCode {

    OK("200", "OK"),
    FOUND("302", "Found"),
    NOT_FOUND("404", "Not Found"),
    METHOD_NOT_ALLOWED("405", "Method Not Allowed");

    final String status;
    final String message;

    HttpCode(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public static String getMessage(String statusCode) {
        for (HttpCode httpCode : values()) {
            if (httpCode.status.equals(statusCode)) {
                return httpCode.message;
            }
        }
        return statusCode;
    }

    public static HttpCode of(String statusCode) {
        for (HttpCode httpCode : values()) {
            if (httpCode.status.equals(statusCode)) {
                return httpCode;
            }
        }
        return HttpCode.NOT_FOUND;
    }

    public String getStatus() {
        return status;
    }
}
