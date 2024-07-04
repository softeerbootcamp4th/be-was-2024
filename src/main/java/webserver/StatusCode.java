package webserver;

public enum StatusCode {
    OK("OK","200"),
    NOT_FOUND("NOT_FOUND","404"),
    ERROR("ERROR","500");
    private String code;
    private String message;
    StatusCode(String message,String code) {
        this.code = code;
        this.message = message;
    }

    public static String getCode(String message) {
        for (StatusCode statusCode : StatusCode.values()) {
            if(statusCode.message.equals(message)){
                return statusCode.code;
            }
        }
        return ERROR.code;
    }
}
