package util;

import java.util.HashMap;
import java.util.Map;



/**
 * HttpHeader값을 담은 enum
 */
public enum HttpHeader {

    ACCEPT("Accept"),
    ACCEPT_ENCODING("Accept-Encoding"),
    ACCEPT_LANGUAGE("Accept-Language"),
    AUTHORIZATION("Authorization"),
    CACHE_CONTROL("Cache-Control"),
    CONNECTION("Connection"),
    CONTENT_LENGTH("Content-Length"),
    CONTENT_TYPE("Content-Type"),
    COOKIE("Cookie"),
    HOST("Host"),
    USER_AGENT("User-Agent"),
    LOCATION("Location"),
    SET_COOKIE("Set-Cookie");

    private final String headerName;

    private static final Map<String, HttpHeader> LOOKUP = new HashMap<>();

    static {
        for (HttpHeader httpHeader : HttpHeader.values()) {
            LOOKUP.put(httpHeader.getHeaderName().toLowerCase(), httpHeader);
        }
    }

    HttpHeader(String headerName) {
        this.headerName = headerName;
    }


    /**
     * headerName Getter
     */
    public String getHeaderName() {
        return headerName;
    }


    /**
     * 들어온 headerName값을 실제 HttpHeader String으로 변환해준다
     */
    public static HttpHeader fromString(String headerName) {
        return LOOKUP.get(headerName.toLowerCase());
    }
}
