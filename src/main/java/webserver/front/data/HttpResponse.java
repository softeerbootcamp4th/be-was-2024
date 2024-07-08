package webserver.front.data;

import java.io.IOException;
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
        this.httpVersion = httpVersion;
        this.statusCode = statusCode;
        this.statusText = statusText;
        this.body = body;
        this.contentType = contentType;
        this.contentLength   = body.length;
        putMap(httpVersion, statusCode, statusText, body, contentType);
    }

    private void putMap(String httpVersion, String statusCode, String statusText, byte[] body, String contentType) {
        headerInformations.put("httpVersion", httpVersion);
        headerInformations.put("statusCode", statusCode);
        headerInformations.put("statusText", statusText);
        headerInformations.put("contentType", contentType);
        headerInformations.put("contentLength", String.valueOf(body.length));
    }
    public void addLocation(String location){
        headerInformations.put("location", location);
    }
    public HashMap<String, String> getHeaderInformations() {
        return headerInformations;
    }

    public byte[] getBody() {
        return body;
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

