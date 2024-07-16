package enums;

public enum HttpCode {
    OK(200, HttpResult.SUCCESS, "OK"),
    Found(302, HttpResult.REDIRECT, "Found"),
    UNPROCESSABLE_CONTENT(422, HttpResult.CLIENT_ERROR, "Unprocessable Content"),
    BAD_REQUEST(400, HttpResult.CLIENT_ERROR, "Bad Request"),
    UNAUTHORIZED(401, HttpResult.CLIENT_ERROR, "Unauthorized");

    private int code;
    private HttpResult httpResult;
    private String message;

    HttpCode(int code, HttpResult httpResult, String message) {
        this.code = code;
        this.httpResult = httpResult;
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

    public boolean isRedirect() {
        return httpResult.equals(HttpResult.REDIRECT);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
    public HttpResult getHttpResult() {
        return httpResult;
    }
}
