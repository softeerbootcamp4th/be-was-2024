package util;

import exception.RequestException;
import session.Session;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HTTP 요청에 대한 정보를 저장하는 클래스
 */
public class HttpRequest {

    private String requestMethod;
    private String requestPath;
    private Map<String, String> requestParams;
    private String httpVersion;
    private Map<String, String> requestHeaders = new HashMap<>();
    private byte[] requestBody;
    private Map<String, String> formData = new HashMap<>();
    private Map<String, byte[]> fileData = new HashMap<>();
    private Session session;

    private HttpRequest(String requestMethod, String requestPath, Map<String, String> requestParams, String httpVersion) {
        this.requestMethod = requestMethod;
        this.requestPath = requestPath;
        this.requestParams = requestParams;
        this.httpVersion = httpVersion;
    }

    /**
     * RequestLine을 파싱하여 HttpRequest 객체를 최초로 생성하는 메서드
     * @param requestLine
     * @return HttpRequest
     */
    public static HttpRequest from(String requestLine) {
        String[] requestLineElements = Arrays.stream(requestLine.split(ConstantUtil.SPACES)).map(String::trim).toArray(String[]::new);
        String requestMethod = requestLineElements[0];
        String[] requestURIElements = Arrays.stream(requestLineElements[1].split(ConstantUtil.QUESTION_MARK)).map(String::trim).toArray(String[]::new);
        String requestPath = requestURIElements[0];
        Map<String, String> requestParams = new HashMap<>();
        if(requestURIElements.length > 1) {
            String[] params = requestURIElements[1].split(ConstantUtil.AND);
            for (String param : params) {
                String[] keyValue = param.split(ConstantUtil.EQUAL);
                requestParams.put(keyValue[0], URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8));
            }
        }
        String requestVersion = requestLineElements[2];
        return new HttpRequest(requestMethod, requestPath, requestParams, requestVersion);
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public String getParameter(String key) {
        return requestParams.getOrDefault(key, "");
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public int getContentLength(){
        return Integer.parseInt(requestHeaders.getOrDefault(ConstantUtil.CONTENT_LENGTH, "0"));
    }

    public String getContentType(){
        return requestHeaders.getOrDefault(ConstantUtil.CONTENT_TYPE, "");
    }

    public String getHeader(String key) {
        return requestHeaders.getOrDefault(key, "");
    }

    public byte[] getBody(){
        return requestBody;
    }

    public Map<String, String> getFormData() {
        return formData;
    }

    public Map<String, byte[]> getFileData() {
        return fileData;
    }

    public Session getSession(){
        return session;
    }

    /**
     * Request Body를 Map으로 변환하는 메서드
     * @return
     */
    public Map<String, String> getBodyMap() {
        Map<String, String> bodyMap = new HashMap<>();

        // byte[] to String, "&"으로 split
        String restoredString = new String(requestBody, StandardCharsets.UTF_8);
        String[] pairs = restoredString.split(ConstantUtil.AND);
        // 만약 body가 없는 경우
        if(pairs.length == 1 && pairs[0].isEmpty()) throw new RequestException(ConstantUtil.INVALID_BODY);

        // 디코딩한 후 "="으로 split하여 Map에 저장
        for (String pair : pairs) {
            String[] keyValue = pair.split(ConstantUtil.EQUAL);
            if(keyValue.length != 2) throw new RequestException(ConstantUtil.INVALID_BODY);
            String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
            String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
            bodyMap.put(key.trim(), value.trim());
        }
        return bodyMap;
    }

    /**
     * 클라이언트의 쿠키로부터 파싱한 세션을 주입하는 메서드 (내부 서비스에서 활용하기 위함)
     */
    public void putSession(Session session){
        this.session = session;
    }

    /**
     * Request Header를 Map으로 변환하는 메서드
     * @param headerLine
     */
    public void putHeaders(String headerLine){
        if(headerLine.isEmpty()) return;
        headerLine = headerLine.replaceAll(ConstantUtil.SPACES, ConstantUtil.SPACE); // remove multiple spaces
        int elementsCount = headerLine.split(ConstantUtil.COLON).length;

        // 콜론이 없는 경우
        if(elementsCount == 1) {
            throw new RequestException(ConstantUtil.INVALID_HEADER);
        }
        // 특정 헤더가 아닌데 3개 이상의 콜론을 갖는 경우
        if(elementsCount > 3 && !headerLine.startsWith("Referer") && !headerLine.startsWith("Origin")){
            throw new RequestException(ConstantUtil.INVALID_HEADER);
        }

        int idx = headerLine.indexOf(ConstantUtil.COLON);
        // "Host localhost:8080" 같은 경우
        if(headerLine.substring(0, idx).contains("localhost") && elementsCount == 2){
            throw new RequestException(ConstantUtil.INVALID_HEADER);
        }
        String[] header = {headerLine.substring(0, idx), headerLine.substring(idx + 1)};
        requestHeaders.put(header[0].toLowerCase().trim(), header[1].trim());
    }

    /**
     * Byte 배열로 입력되는 body를 byte[]로 저장하며, 이 때 UTF-8로 디코딩하여 저장
     * @param body
     */
    public void putBody(List<Byte> body){
        // List<Byte> to byte[]
        byte[] byteArray = new byte[body.size()];
        for (int i = 0; i < body.size(); i++) {
            byteArray[i] = body.get(i);
        }
        // byte[] to String 후 디코딩하여 다시 byte[]로 변환
        this.requestBody = new String(byteArray, StandardCharsets.UTF_8).getBytes();
    }

    public void putBody(byte[] body){
        this.requestBody = body;
    }

    public void putFormData(String key, String value) {
        formData.put(key, value);
    }

    public void putFileData(String key, byte[] value) {
        fileData.put(key, value);
    }
}
