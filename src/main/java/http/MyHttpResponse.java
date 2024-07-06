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
        this.body = new byte[0];
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
     * @return 바이트로 구성된 바디 부분
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
     * @param body 작성할 byte 데이터
     */
    public void setBody(byte[] body) {
        this.body = body != null ? body : new byte[0];
        getHeaders().putHeader("Content-Length", String.valueOf(body.length));
    }

    /**
     * <pre>
     * body에 데이터를 쓴다. Content-Length 헤더를 함께 업데이트한다.
     * 문자열을 바이트로 변환하여 body에 작성한다.
     * </pre>
     * @param body String 타입의 바디
     */
    public void setBody(String body) {
        byte[] bodyBytes = body.getBytes();
        this.setBody(bodyBytes);
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
