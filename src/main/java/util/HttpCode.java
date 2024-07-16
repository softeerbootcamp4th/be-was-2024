package util;

public enum HttpCode {

    OK("200", "OK"),
    FOUND("302", "Found"),
    NOT_FOUND("404", "Not Found"),
    METHOD_NOT_ALLOWED("405", "Method Not Allowed"),
    INTERNAL_SERVER_ERROR("500", "Internal Server Error");

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

    public String getStatus() {
        return status;
    }

    @Override
    public String toString(){
        return status + ConstantUtil.SPACE + message;
    }
}
