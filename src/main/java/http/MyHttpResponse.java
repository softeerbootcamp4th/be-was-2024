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

    /**
     * 응답 바디 부분을 반환한다. (직접 변경은 권장 X)
     * @return
     */
    public byte[] getBody() {
        return body;
    }

    /**
     * 응답 상태를 지정한다
     * @param statusInfo 응답 상태
     */
    public void setStatusInfo(HttpStatusType statusInfo) {
        this.statusInfo = statusInfo;
    }

    public HttpStatusType getStatusInfo() {
        return statusInfo;
    }

    /**
     * body에 데이터를 쓴다. Content-Length 헤더를 함께 업데이트한다.
     * @param body
     */
    public void setBody(byte[] body) {
        this.body = body;
        getHeaders().putHeader("Content-Length", String.valueOf(body.length));
    }

    /**
     * location 경로로 redirect 한다.
     * @param location redirect 할 경로
     */
    public void redirect(String location) {
        setStatusInfo(HttpStatusType.FOUND);
        getHeaders().putHeader("Location", location);
    }
}
