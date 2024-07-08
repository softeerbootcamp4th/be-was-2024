package util;

public enum HttpStatusCode {

    OK("200", "OK"),
    FOUND("302", "Found"),
    NOT_FOUND("404", "Not Found"),
    METHOD_NOT_ALLOWED("405", "Method Not Allowed");

    final String status;
    final String message;

    HttpStatusCode(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public static String getHttpStatusMessage(String statusCode) {
        for (HttpStatusCode httpStatusCode : values()) {
            if (httpStatusCode.status.equals(statusCode)) {
                return httpStatusCode.message;
            }
        }
        return statusCode;
    }

    public static HttpStatusCode getStatus(String statusCode) {
        for(HttpStatusCode httpStatusCode : values()) {
            if(httpStatusCode.status.equals(statusCode)) {
                return httpStatusCode;
            }
        }
        return NOT_FOUND;
    }
}
