package util;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP 응답에 대한 정보를 관리하는 클래스
 */
public class HttpResponse {

    private String type;
    private String httpVersion;
    private String path;
    private String statusCode;
    private Map<String, String> header = new HashMap<>();
    private byte[] body;

    protected HttpResponse() {
    }

    /**
     * 200 OK 응답 생성하는 정적 팩토리 메서드
     * @param path
     * @param httpVersion
     * @param body
     * @return HttpResponse
     */
    public static HttpResponse ok(String path, String httpVersion, String body) {
        HttpResponse response = new HttpResponse();
        response.type = ConstantUtil.DYNAMIC;
        response.path = path;
        response.statusCode = HttpCode.OK.getStatus();
        response.httpVersion = httpVersion;
        response.putBody(body);
        response.putHeader(ConstantUtil.CONTENT_LENGTH, response.getBody().length + "");
        if(path.contains(ConstantUtil.DOT)){
            String[] element = path.split(ConstantUtil.REGDOT);
            response.putHeader(ConstantUtil.CONTENT_TYPE, ContentType.getType(element[element.length - 1]));
        } else {
            response.putHeader(ConstantUtil.CONTENT_TYPE, ContentType.HTML.getExtension());
        }
        return response;
    }

    /**
     * 200 OK 정적 응답 생성하는 정적 팩토리 메서드
     * staticResponse에서 헤더 처리를 담당하기에 Header/Body를 설정하지 않음
     * @param path
     * @param httpVersion
     * @return HttpResponse
     */
    public static HttpResponse okStatic(String path, String httpVersion) {
        HttpResponse response = new HttpResponse();
        response.type = ConstantUtil.STATIC;
        response.path = path;
        response.statusCode = HttpCode.OK.getStatus();
        response.httpVersion = httpVersion;
        return response;
    }

    /**
     * 404 Not Found 응답 생성하는 정적 팩토리 메서드
     * @param httpVersion
     * @return HttpResponse
     */
    public static HttpResponse notFound(String httpVersion) {
        HttpResponse response = new HttpResponse();
        response.type = ConstantUtil.FAULT;
        response.statusCode = HttpCode.NOT_FOUND.getStatus();
        response.httpVersion = httpVersion;
        response.putBody(HttpCode.NOT_FOUND.toString());
        return response;
    }

    /**
     * 405 Method Not Allowed 응답 생성하는 정적 팩토리 메서드
     * @param httpVersion
     * @return HttpResponse
     */
    public static HttpResponse methodNotAllowed(String httpVersion) {
        HttpResponse response = new HttpResponse();
        response.type = ConstantUtil.FAULT;
        response.statusCode = HttpCode.METHOD_NOT_ALLOWED.getStatus();
        response.httpVersion = httpVersion;
        response.putBody(HttpCode.METHOD_NOT_ALLOWED.toString());
        return response;
    }

    /**
     * 500 Internal Server Error 응답 생성하는 정적 팩토리 메서드
     * @param body
     * @param httpVersion
     * @return HttpResponse
     */
    public static HttpResponse error(String body, String httpVersion) {
        HttpResponse response = new HttpResponse();
        response.type = ConstantUtil.FAULT;
        response.statusCode = HttpCode.INTERNAL_SERVER_ERROR.getStatus();
        response.httpVersion = httpVersion;
        response.putBody(body);
        return response;
    }

    /**
     * 302 Found 응답 생성하는 정적 팩토리 메서드
     * @param path
     * @param httpVersion
     * @return HttpResponse
     */
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

    public String getHttpVersion() {
        return httpVersion;
    }

    public byte[] getBody(){
        return body;
    }

    /**
     * 모든 헤더를 반환하는 메서드
     * @return String
     */
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

    /**
     * 헤더를 추가
     * @param key
     * @param value
     */
    private void putHeader(String key, String value){
        header.put(key, value);
    }

    /**
     * 바디를 추가
     * @param body
     */
    private void putBody(String body){
        this.body = body.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 세션 ID 설정
     * @param sessionId
     */
    public void setSessionId(String sessionId){
        putHeader(ConstantUtil.SET_COOKIE, sessionId);
    }

    /**
     * 세션 ID 삭제
     * @param sessionId
     */
    public void deleteSessionId(String sessionId){
        putHeader(ConstantUtil.SET_COOKIE, "sid=" + sessionId + "; Path=/; Max-Age=0");
    }
}
