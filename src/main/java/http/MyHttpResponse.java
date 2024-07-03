package http;

import http.enums.HttpStatusType;

public class MyHttpResponse {
    private final String version;
    private HttpStatusType statusInfo;
    private final MyHttpHeaders headers;

    private byte[] body;

    public MyHttpResponse(String version) {
        this.version = version;
        this.headers = new MyHttpHeaders();
    }

    public String getVersion() {
        return version;
    }

    public HttpStatusType getStatusType() {
        return statusInfo;
    }

    public MyHttpHeaders getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    public void setStatusInfo(HttpStatusType statusInfo) {
        this.statusInfo = statusInfo;
    }

    public HttpStatusType getStatusInfo() {
        return statusInfo;
    }

    // 바디를 쓰면 Content-Length도 함께 설정해주기
    public void setBody(byte[] body) {
        this.body = body;
        getHeaders().putHeader("Content-Length", String.valueOf(body.length));
    }
}
