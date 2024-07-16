package web;

public enum ResponseCode {
    // 2xx
    OK("200", "OK"),

    // 3xx
    FOUND("302", "Found"),

    // 4xx
    BAD_REQUEST("400", "Bad Request"),
    UNAUTHORIZED("401", "Unauthorized"),
    FORBIDDEN("403", "Forbidden"),
    NOT_FOUND("404", "Not Found"),

    // 5xx
    INTERNAL_SERVER_ERROR("500", "Internal Server Error");

    private final String code;
    private final String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static ResponseCode findByCode(String code) {
        for (ResponseCode responseCode : ResponseCode.values()) {
            if (responseCode.code.equals(code)) {
                return responseCode;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }

    public static ResponseCode findByMessage(String message) {
        for (ResponseCode responseCode : ResponseCode.values()) {
            if (responseCode.message.equals(message)) {
                return responseCode;
            }
        }
        throw new IllegalArgumentException("Unknown message: " + message);
    }
}
