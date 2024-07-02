package http;

import http.utils.HttpMethodTypeUtil;
import http.utils.HttpParseUtil;

import java.util.List;
import java.util.Map;

public class HttpRequest {
    private HttpMethodType method;
    private String url;
    private String version;

    MyHttpHeaders headers;

    public HttpRequest(String requestLine, List<String> headerLines) {
        String[] reqLineTokens = HttpParseUtil.ParseRequestLine(requestLine);
        this.method = HttpMethodTypeUtil.getHttpMethodType(reqLineTokens[0]);
        this.url = reqLineTokens[1];
        this.version = reqLineTokens[2];

        headers = new MyHttpHeaders();
        headers.PutHeaders(headerLines);
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
        return headers.GetHeaders();
    }
}
