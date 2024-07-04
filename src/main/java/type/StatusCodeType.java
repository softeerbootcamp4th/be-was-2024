package type;

public enum StatusCodeType {
    // [200, 300)
    OK(200, "OK"),
    // [300, 400)
    FOUND(302, "Found"),
    // [400, 500)
    BAD_REQUEST(400, "Bad Request"),
    NOT_FOUND(404, "Not Found");

    private final int code;
    private final String text;

    StatusCodeType(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public int getCode() { return code; }

    public String getText() { return text; }
}
