package http;

import http.enums.HttpMethodType;
import http.utils.HttpMethodTypeUtil;
import http.utils.HttpParseUtil;
import url.MyURL;

import java.util.List;

public class MyHttpRequest {
    private final HttpMethodType method;
    private final MyURL url;
    private final String version;

    private final MyHttpHeaders headers;
    private byte[] body;

    public MyHttpRequest(HttpMethodType method, MyURL url, String version, MyHttpHeaders headers, byte[] body) {
        this.method = method;
        this.url = url;
        this.version = version;
        this.headers = headers;
        this.body = body;
    }

    public MyHttpRequest(String requestLine, List<String> headerLines, byte[] body) {
        String[] reqLineTokens = HttpParseUtil.parseRequestLine(requestLine);
        this.method = HttpMethodTypeUtil.getHttpMethodType(reqLineTokens[0]);
        this.url = new MyURL(reqLineTokens[1]);
        this.version = reqLineTokens[2];

        headers = new MyHttpHeaders();
        headers.putHeaders(headerLines);
        // body는 초기화하지 않은 상태로 취급. 나중에 설정해야 함.
        this.body = body;
    }

    public MyHttpRequest(String requestLine, MyHttpHeaders headers, byte[] body) {
        String[] reqLineTokens = HttpParseUtil.parseRequestLine(requestLine);
        this.method = HttpMethodTypeUtil.getHttpMethodType(reqLineTokens[0]);
        this.url = new MyURL(reqLineTokens[1]);
        this.version = reqLineTokens[2];

        this.headers = headers;
        this.body = body;
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
}
