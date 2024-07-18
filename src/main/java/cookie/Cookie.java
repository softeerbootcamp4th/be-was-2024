package cookie;

public interface Cookie {
    static final String headerName = "Set-Cookie";
    static final String CRLF = "\r\n";

    public String getCookieString();
}
