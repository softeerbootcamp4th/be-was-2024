package util;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private String type;
    private String httpVersion;
    private String path;
    private String statusCode;
    private Map<String, String> header = new HashMap<>();
    private byte[] body;

    protected HttpResponse() {
    }

    public static HttpResponse ok(String type, String path, String statusCode, String httpVersion, String body) {
        HttpResponse response = new HttpResponse();
        response.type = type;
        response.path = path;
        response.statusCode = statusCode;
        response.httpVersion = httpVersion;
        response.putBody(body);
        response.putHeader("Content-Type", ContentType.getType(path.contains(StringUtil.DOT) ? path.split(StringUtil.REGDOT)[1] : String.valueOf(ContentType.HTML)));
        response.putHeader("Content-Length", response.getBody().length + "");
        return response;
    }

    // staticResponse에서 담당하기에 Header/Body를 설정하지 않음
    public static HttpResponse okStatic(String path, String statusCode, String httpVersion) {
        HttpResponse response = new HttpResponse();
        response.type = StringUtil.STATIC;
        response.path = path;
        response.statusCode = statusCode;
        response.httpVersion = httpVersion;
        return response;
    }

    public static HttpResponse error(String statusCode, String httpVersion) {
        HttpResponse response = new HttpResponse();
        response.type = StringUtil.FAULT;
        response.statusCode = statusCode;
        response.httpVersion = httpVersion;
        return response;
    }

    public static HttpResponse redirect(String path, String statusCode, String httpVersion) {
        HttpResponse response = new HttpResponse();
        response.type = StringUtil.DYNAMIC;
        response.path = path;
        response.statusCode = statusCode;
        response.httpVersion = httpVersion;
        response.putHeader("Location", path);
        return response;
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

    private void putHeader(String key, String value){
        header.put(key, value);
    }

    private void putBody(String body){
        this.body = body.getBytes();
    }

    public void setSessionId(String sessionId){
        putHeader("Set-Cookie", "sid=" + sessionId + "; Path=/");
    }

    public void deleteSessionId(String sessionId){
        putHeader("Set-Cookie", "sid=" + sessionId + "; Path=/; Max-Age=0");
    }
}
