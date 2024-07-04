package type;

public enum StatusCodeType {
    OK(200, "OK"),
    FOUND(302, "Found"),
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
