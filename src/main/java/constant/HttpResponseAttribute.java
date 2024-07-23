package constant;

/**
 * HttpResponse의 속성 및 값을 관리하는 Enum
 */
public enum HttpResponseAttribute {

    CONTENT_TYPE("Content-Type"),
    CONTENT_LENGTH("Content-Length"),
    LOCATION("Location"),
    CHARSET_UTF8("utf-8");

    final String value;

    HttpResponseAttribute(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
