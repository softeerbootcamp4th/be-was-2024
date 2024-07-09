package util;

import java.util.HashMap;
import java.util.Map;

public class HttpResponseObject {

    private String type;
    private String httpVersion;
    private String path;
    private String statusCode;
    private Map<String, String> header;
    private byte[] body;

    public HttpResponseObject(String type, String path, String statusCode, String httpVersion) {
        this.type = type;
        this.path = path;
        this.statusCode = statusCode;
        this.httpVersion = httpVersion;
        this.header = new HashMap<>();
    }

    public String getType() {
        return type;
    }

    public String getPath() {
        return path;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public byte[] getBody(){
        return body;
    }

    public String getTotalHeaders(){
        StringBuilder sb = new StringBuilder();
        sb.append(httpVersion).append(StringUtil.SPACE).append(statusCode).append(StringUtil.SPACE).append(HttpCode.getMessage(statusCode)).append(StringUtil.CRLF);
        for (Map.Entry<String, String> entry : header.entrySet()) {
            sb.append(entry.getKey()).append(StringUtil.COLON_WITH_SPACE).append(entry.getValue()).append(StringUtil.CRLF);
        }
        sb.append(StringUtil.CRLF);
        return sb.toString();
    }

    public void putHeader(String key, String value){
        header.put(key, value);
    }

    public void putBody(String body){
        this.body = body.getBytes();
    }
}
