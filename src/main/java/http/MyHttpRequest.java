package http;

import http.cookie.MyCookies;
import http.enums.HttpMethodType;
import http.utils.HttpMethodTypeUtil;
import http.utils.HttpParseUtil;
import url.MyURL;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyHttpRequest {
    private final HttpMethodType method;
    private final MyURL url;
    private final String version;

    private final MyHttpHeaders headers;
    private final MyCookies cookies;

    /**
     * 미들웨어 사이에서 요청을 통해 공유할 수 있는 데이터 저장소
     */
    private final Map<String, Object> store;

    private byte[] body;

    public MyHttpRequest(HttpMethodType method, MyURL url, String version, MyHttpHeaders headers, byte[] body) {
        this.method = method;
        this.url = url;
        this.version = version;
        this.headers = headers;
        this.cookies = HttpParseUtil.parseCookies(headers.getHeader("Cookie"));
        this.body = body;
        this.store = new HashMap<>();
    }

    public MyHttpRequest(HttpMethodType method, MyURL url, String version, MyHttpHeaders headers, MyCookies cookies, byte[] body) {
        this.method = method;
        this.url = url;
        this.version = version;
        this.headers = headers;
        this.cookies = cookies;
        this.body = body;
        this.store = new HashMap<>();
    }

    public MyHttpRequest(String requestLine, List<String> headerLines, byte[] body) {
        String[] reqLineTokens = HttpParseUtil.parseRequestLine(requestLine);
        this.method = HttpMethodTypeUtil.getHttpMethodType(reqLineTokens[0]);
        this.url = new MyURL(reqLineTokens[1]);
        this.version = reqLineTokens[2];

        headers = new MyHttpHeaders();
        headers.putHeaders(headerLines);
        this.cookies = HttpParseUtil.parseCookies(headers.getHeader("Cookie"));
        // body는 초기화하지 않은 상태로 취급. 나중에 설정해야 함.
        this.body = body;
        this.store = new HashMap<>();
    }

    public MyHttpRequest(String requestLine, MyHttpHeaders headers, byte[] body) {
        String[] reqLineTokens = HttpParseUtil.parseRequestLine(requestLine);
        this.method = HttpMethodTypeUtil.getHttpMethodType(reqLineTokens[0]);
        this.url = new MyURL(reqLineTokens[1]);
        this.version = reqLineTokens[2];

        this.headers = headers;
        this.cookies = HttpParseUtil.parseCookies(headers.getHeader("Cookie"));
        this.body = body;
        this.store = new HashMap<>();
    }

    public MyURL getUrl() {
        return url;
    }

    public HttpMethodType getMethod() {
        return method;
    }

    public String getVersion() {
        return version;
    }

    public MyHttpHeaders getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public MyCookies getCookies() {
        return cookies;
    }

    public Object getStoreData(String key) {
        return store.get(key);
    }

    public void setStoreData(String key, Object value) {
        store.put(key, value);
    }

    /**
     * 외부에서 변경하지 못하는 상태로, 단지 데이터만 전달하기 위한 목적으로 사용. 변경을 원한다면 setStoreData 사용
     * @return 세션 맵.
     */
    public Map<String, Object> getStore() {
        return Collections.unmodifiableMap(store);
    }
}
