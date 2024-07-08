package util;

import java.util.HashMap;
import java.util.Map;

public class HttpResponseObject {

    private String type;
    private String httpVersion;
    private String path;
    private String statusCode;
    private Map<String, String> header;
    private Map<String, String> body;

    public HttpResponseObject(String type, String path, String statusCode, String httpVersion, Map<String, String> responseBody) {
        this.type = type;
        this.path = path;
        this.statusCode = statusCode;
        this.httpVersion = httpVersion;
        this.header = new HashMap<>();
        this.body = (responseBody == null ? new HashMap<>() : responseBody);
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

    public Map<String, String> getBody(){
        return body;
    }

    public byte[] getBodyToByte() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : body.entrySet()) {
            sb.append(entry.getKey()).append(StringUtil.COLON_WITH_SPACE).append(entry.getValue()).append(StringUtil.CRLF);
        }
        return sb.toString().getBytes();
    }

    public void addHeader(String key, String value){
        header.put(key, value);
    }

    public String getTotalHeaders(){
        StringBuilder sb = new StringBuilder();
        sb.append(httpVersion).append(StringUtil.SPACE).append(statusCode).append(StringUtil.SPACE).append(HttpStatusCode.getHttpStatusMessage(statusCode)).append(StringUtil.CRLF);
        for (Map.Entry<String, String> entry : header.entrySet()) {
            sb.append(entry.getKey()).append(StringUtil.COLON_WITH_SPACE).append(entry.getValue()).append(StringUtil.CRLF);
        }
        sb.append(StringUtil.CRLF);
        return sb.toString();
    }
}
