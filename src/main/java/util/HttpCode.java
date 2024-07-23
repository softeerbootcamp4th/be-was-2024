package util;

/**
 * HTTP 응답 코드를 정의한 enum 클래스
 */
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

    /**
     * 응답 코드에 해당하는 메시지를 반환하는 메서드
     * @param statusCode
     * @return String
     */
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

    /**
     * 응답 코드와 메시지를 묶어 문자열로 반환하는 메서드
     * @return String
     */
    @Override
    public String toString(){
        return status + ConstantUtil.SPACE + message;
    }
}
