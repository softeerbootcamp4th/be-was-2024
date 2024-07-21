package cookie;

/**
 * Cookie 인터페이스
 */
public interface Cookie {
    String headerName = "Set-Cookie";
    String CRLF = "\r\n";

    String getCookieString();
}
