package util;

import exception.RequestException;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {

    private String requestMethod;
    private String requestPath;
    private Map<String, String> requestParams;
    private String httpVersion;
    private Map<String, String> requestHeaders = new HashMap<>();
    private byte[] requestBody;

    private HttpRequest(String requestMethod, String requestPath, Map<String, String> requestParams, String httpVersion) {
        this.requestMethod = requestMethod;
        this.requestPath = requestPath;
        this.requestParams = requestParams;
        this.httpVersion = httpVersion;
    }

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

    public Map<String, String> getRequestParams() {
        return requestParams;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    public byte[] getBody(){
        return requestBody;
    }

    public String getBodyString() {
        return new String(requestBody, StandardCharsets.UTF_8);
    }

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
            String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
            String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
            bodyMap.put(key.trim(), value.trim());
        }
        return bodyMap;
    }

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
        requestHeaders.put(header[0].toLowerCase().trim(), header[1].toLowerCase().trim());
    }

    public void putBody(List<Byte> body){
        // List<Byte> to byte[]
        byte[] byteArray = new byte[body.size()];
        for (int i = 0; i < body.size(); i++) {
            byteArray[i] = body.get(i);
        }
        // byte[] to String 후 디코딩하여 다시 byte[]로 변환
        this.requestBody = new String(byteArray, StandardCharsets.UTF_8).getBytes();
    }
}
