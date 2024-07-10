package http.cookie.enums;

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
