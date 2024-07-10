package type;

public enum HTTPStatusCode {
    // 2XX
    OK(200, "OK"),
    // 3XX
    FOUND(302, "Found"),
    // 4XX
    BAD_REQUEST(400, "Bad Request"),
    NOT_FOUND(404, "Not Found");

    private final int code;
    private final String text;

    HTTPStatusCode(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public int getCode() { return code; }

    public String getText() { return text; }
}
