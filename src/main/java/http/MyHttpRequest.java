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

    public MyHttpRequest(String requestLine, List<String> headerLines) {
        String[] reqLineTokens = HttpParseUtil.parseRequestLine(requestLine);
        this.method = HttpMethodTypeUtil.getHttpMethodType(reqLineTokens[0]);
        this.url = new MyURL(reqLineTokens[1]);
        this.version = reqLineTokens[2];

        headers = new MyHttpHeaders();
        headers.putHeaders(headerLines);
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
}
