package util;

import java.nio.charset.StandardCharsets;
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

    public static HttpResponse ok(String type, String path, String httpVersion, String body) {
        HttpResponse response = new HttpResponse();
        response.type = type;
        response.path = path;
        response.statusCode = HttpCode.OK.getStatus();
        response.httpVersion = httpVersion;
        response.putBody(body);
        response.putHeader(ConstantUtil.CONTENT_TYPE, ContentType.getType(path.contains(ConstantUtil.DOT) ? path.split(ConstantUtil.REGDOT)[1] : String.valueOf(ContentType.HTML)));
        response.putHeader(ConstantUtil.CONTENT_LENGTH, response.getBody().length + "");
        return response;
    }

    // staticResponse에서 담당하기에 Header/Body를 설정하지 않음
    public static HttpResponse okStatic(String path, String httpVersion) {
        HttpResponse response = new HttpResponse();
        response.type = ConstantUtil.STATIC;
        response.path = path;
        response.statusCode = HttpCode.OK.getStatus();
        response.httpVersion = httpVersion;
        return response;
    }

    public static HttpResponse error(String statusCode, String httpVersion) {
        HttpResponse response = new HttpResponse();
        response.type = ConstantUtil.FAULT;
        response.statusCode = statusCode;
        response.httpVersion = httpVersion;
        return response;
    }

    public static HttpResponse redirect(String path, String httpVersion) {
        HttpResponse response = new HttpResponse();
        response.type = ConstantUtil.DYNAMIC;
        response.path = path;
        response.statusCode = HttpCode.FOUND.getStatus();
        response.httpVersion = httpVersion;
        response.putHeader(ConstantUtil.LOCATION, path);
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
        sb.append(httpVersion).append(ConstantUtil.SPACE)
            .append(statusCode).append(ConstantUtil.SPACE)
            .append(HttpCode.getMessage(statusCode)).append(ConstantUtil.CRLF);

        for (Map.Entry<String, String> entry : header.entrySet()) {
            sb.append(entry.getKey()).append(ConstantUtil.COLON_WITH_SPACE).append(entry.getValue()).append(ConstantUtil.CRLF);
        }
        sb.append(ConstantUtil.CRLF);
        return sb.toString();
    }

    private void putHeader(String key, String value){
        header.put(key, value);
    }

    private void putBody(String body){
        this.body = body.getBytes(StandardCharsets.UTF_8);
    }

    public void setSessionId(String sessionId){
        putHeader(ConstantUtil.SET_COOKIE, "sid=" + sessionId + "; Path=/");
    }

    public void deleteSessionId(String sessionId){
        putHeader("Set-Cookie", "sid=" + sessionId + "; Path=/; Max-Age=0");
    }
}
