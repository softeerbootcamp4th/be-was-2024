package Cookie;

public enum CookieAttribute {
    SESSION_ID("sessionId"),
    DOMAIN("Domain"),
    PATH("Path"),
    MAX_AGE("Max-Age"),
    SECURE("Secure"),
    HTTPONLY("HttpOnly");

    final String attributeName;

    CookieAttribute(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeName() {
        return attributeName;
    }

}
