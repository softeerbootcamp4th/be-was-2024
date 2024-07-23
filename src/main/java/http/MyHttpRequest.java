package http;

import config.AppConfig;
import http.cookie.MyCookies;
import http.enums.HttpMethodType;
import http.utils.HttpMethodTypeUtil;
import http.utils.HttpParseUtil;
import url.MyURL;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * http request 를 표현하는 클래스
 */
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
        this.cookies = HttpParseUtil.parseCookies(headers.getHeader(HeaderConst.Cookie));
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
        this.cookies = HttpParseUtil.parseCookies(headers.getHeader(HeaderConst.Cookie));
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
        this.cookies = HttpParseUtil.parseCookies(headers.getHeader(HeaderConst.Cookie));
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

    /**
     * 요청에 임시 저장된 데이터를 가져온다
     * @param key 저장한 데이터의 식별자
     * @return 저장된 데이터
     */
    public Object getStoreData(String key) {
        return store.get(key);
    }

    /**
     * 요청에 임시로 데이터를 저장한다.
     * @param key 저장할 데이터의 식별자
     * @param value 저장할 데이터
     */
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

    /**
     * path variable을 얻는다.
     */
    public String getPathVariable(String key) {
        Object value = store.get(AppConfig.PATHVAR_PREFIX + key);
        return value != null ? (String) value : null;
    }

    /**
     * path variable을 설정한다.
     */
    public void setPathVariable(String key, String value) {
        store.put(AppConfig.PATHVAR_PREFIX + key, value);
    }
}
