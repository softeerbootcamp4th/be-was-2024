package Cookie;

public class Cookie {
    private static final String headerName = "Set-Cookie";
    private static final String CRLF = "\r\n";
    private String sessionId;
    private String domain;
    private String path;
    private String maxAge;
    private boolean secure;
    private boolean httpOnly;

    public Cookie(String sessionId){
        this.sessionId = sessionId;
        domain = "localhost:8080";
        path = "/";
        maxAge = "3600";
        secure = false;
        httpOnly = false;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setMaxAge(String maxAge) {
        this.maxAge = maxAge;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    public String getCookieString(){
        StringBuilder sb = new StringBuilder();
        sb.append(headerName + ": ")
                .append(CookieAttribute.SESSION_ID).append("=").append(sessionId).append(";")
                .append(CookieAttribute.DOMAIN).append("=").append(domain).append(";")
                .append(CookieAttribute.PATH).append("=").append(path).append(";")
                .append(CookieAttribute.MAX_AGE).append("=").append(maxAge).append(";");

        if(secure)
            sb.append(CookieAttribute.SECURE).append(";");

        if(httpOnly)
            sb.append(CookieAttribute.HTTPONLY).append(";");

        sb.append(CRLF);

        return sb.toString();
    }

}
