package cookie;

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
        domain = "localhost";
        path = "/";
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

    public void setMaxAge(int maxAge) {
        this.maxAge = String.valueOf(maxAge);
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
                .append(CookieAttribute.SESSION_ID.getAttributeName()).append("=").append(sessionId).append(";")
                .append(CookieAttribute.PATH.getAttributeName()).append("=").append(path).append(";")
                .append(CookieAttribute.DOMAIN.getAttributeName()).append("=").append(domain).append(";");
        if(maxAge!=null)
            sb.append(CookieAttribute.MAX_AGE.getAttributeName()).append("=").append(maxAge).append(";");

        if(secure)
            sb.append(CookieAttribute.SECURE.getAttributeName()).append(";");

        if(httpOnly)
            sb.append(CookieAttribute.HTTPONLY.getAttributeName()).append(";");

        sb.append(CRLF);

        return sb.toString();
    }

}
