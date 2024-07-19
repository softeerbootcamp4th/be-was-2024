package http.cookie.enums;

/**
 * 쿠키가 가지고 있는 속성을 정의한 열거형
 */
public enum CookieAttribute {
    Domain("Domain"),
    Expires("Expires"),
    MaxAge("Max-Age"),
    Path("Path"),
    HttpOnly("HttpOnly"),
    Secure("Secure");

    private final String attrName;

    CookieAttribute(String attrName) {
        this.attrName = attrName;
    }

    public String getAttrName() {
        return attrName;
    }
}
