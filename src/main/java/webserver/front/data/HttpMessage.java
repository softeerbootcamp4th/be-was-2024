package webserver.front.data;

import java.util.HashMap;
import java.util.Map;

public class HttpMessage {
    private String httpVersion;
    private RepresentationHeader representationHeader;
    private byte[] body;
    public HttpMessage(String httpVersion, byte[] body,String contentType) {
        this.httpVersion = httpVersion;
        this.representationHeader = new RepresentationHeader(contentType,body.length);
        this.body = body;
    }
    public String getHttpVersion() {
        return httpVersion;
    }
    public Map<String, String> getRepresentationHeader() {
        return representationHeader.getData();
    }

    public byte[] getBody() {
        return body;
    }
}
class RepresentationHeader{
    private final Map<String,String> data;
    public RepresentationHeader(String contentType, int contentLength) {
        this.data = new HashMap<>();
        this.data.put("Content-Type", contentType+"; charset=utf-8");
        this.data.put("Content-Length", String.valueOf(contentLength));
    }

    public Map<String, String> getData() {
        return data;
    }
}

