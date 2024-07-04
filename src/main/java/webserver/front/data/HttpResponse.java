package webserver.front.data;

import java.util.HashMap;

public class HttpResponse {
    private HashMap<String, String> headerInformations = new HashMap<>();
    public String httpVersion;
    public String contentType;
    public String statusCode;
    public String statusText;
    public int contentLength;

    public byte[] body;

    public HttpResponse(String httpVersion, String statusCode, String statusText, byte[] body,String contentType) {
        headerInformations.put("httpVersion", httpVersion);
        headerInformations.put("statusCode", statusCode);
        headerInformations.put("statusText", statusText);
        headerInformations.put("contentType", contentType);
        headerInformations.put("contentLength", String.valueOf(body.length));
        this.body = body;
    }
    public String getHttpVersion() {
        return httpVersion;
    }

    public String getContentType() {
        return contentType;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public int getContentLength() {
        return contentLength;
    }
}

