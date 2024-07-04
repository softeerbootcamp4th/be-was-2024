package enums;

public enum HttpCode {
    OK(200, "OK"),
    Found(302, "Found");

    private int code;
    private String message;

    HttpCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static HttpCode getFromHttpCode(int code) {
        HttpCode[] httpCodes = HttpCode.values();
        for(HttpCode httpCode: httpCodes) {
           if(httpCode.code == code) {
              return httpCode;
           }
        }
        throw new IllegalArgumentException("해당 코드와 일치하는 HttpCode는 없습니다.");
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
