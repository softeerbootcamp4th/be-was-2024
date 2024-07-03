package http;

import http.enums.HttpMethodType;
import http.utils.HttpMethodTypeUtil;
import http.utils.HttpParseUtil;

import java.util.List;
import java.util.Map;

public class MyHttpRequest {
    private final HttpMethodType method;
    private final String url;
    private final String version;

    private MyHttpHeaders headers;

    public MyHttpRequest(String requestLine, List<String> headerLines) {
        String[] reqLineTokens = HttpParseUtil.parseRequestLine(requestLine);
        this.method = HttpMethodTypeUtil.getHttpMethodType(reqLineTokens[0]);
        this.url = reqLineTokens[1];
        this.version = reqLineTokens[2];

        headers = new MyHttpHeaders();
        headers.putHeaders(headerLines);
    }

    public String getUrl() {
        return url;
    }

    public HttpMethodType getMethod() {
        return method;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, String> getHeaderMap() {
        return headers.getHeaders();
    }

    public MyHttpHeaders getHeaders() {
        return headers;
    }
}
