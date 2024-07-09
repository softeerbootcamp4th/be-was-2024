package webserver.back.byteReader;

import java.nio.charset.StandardCharsets;

public class RequestBody implements Body {

    private String contentType;
    private String body;
    public RequestBody(String contentType, String body){
        this.contentType = contentType;
        this.body = body;
    }
    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public byte[] makeBytes() {
        return body.getBytes(StandardCharsets.UTF_8);
    }
}
