package http;

import exception.HeaderNotFoundException;
import exception.InvalidHttpRequestException;
import exception.QueryParameterNotFoundException;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

import static util.StringUtil.*;

public class HttpRequest {
    private final Map<String, String> queryParameters = new TreeMap<>();
    private final Map<String, String> headers = new TreeMap<>();
    private byte[] body;

    private String method;
    private String url;
    private String path;
    private String httpVersion;

    public HttpRequest() {}

    public void setMethod(String method) {
        this.method = method;
    }

    public void setUrl(String url) {
        this.url = url;
        setPath(url);
    }
    public void setPath(String url) {
        if(hasQueryParameters(url)) {
            setQueryParameters(url);
            this.path = url.substring(0, url.indexOf(QUESTION_MARK));
        }
        else {
            this.path = url;
        }
    }
    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public String getHeaderValue(String headerName) {
        if(headers.containsKey(headerName)) {
            return headers.get(headerName);
        } else throw new HeaderNotFoundException("Cannot find Header names :  " + headerName);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getQueryParameterValue(String key) throws QueryParameterNotFoundException {
        if(queryParameters.containsKey(key)) {
            return queryParameters.get(key);
        } else throw new QueryParameterNotFoundException("Cannot find Query Parameter names :  " + key);
    }

    private boolean hasQueryParameters(String url){
        return url != null && url.contains(QUESTION_MARK);
    }

    private void setQueryParameters(String url){
        //TODO '?' 는 존재하나 이후에 아무런 값이 없을 경우 IndexOutOfBounds Exception이 발생하는지 확인하고 발생한다면 에러처리 로직 추가.
        String query = url.substring(url.indexOf(QUESTION_MARK) + 1);
        String[] params = query.split(AMPERSAND);
        for(String param : params){
            int index = param.indexOf(EQUALS);
            if(index == -1){
                throw new InvalidHttpRequestException("Invalid query parameter: " + query);
            }
            String key = URLDecoder.decode(param.substring(0, index), StandardCharsets.UTF_8);
            String value = URLDecoder.decode(param.substring(index + 1), StandardCharsets.UTF_8);

            queryParameters.put(key, value);
        }
    }

    private boolean isRootPath(String path){
        return path.equals(ROOT_PATH);
    }

    public String getViewPath() {
        int index = path.lastIndexOf(DOT);
        if(index == -1){
            if(isRootPath(path)) return DEFAULT_VIEW;
            else return path + DEFAULT_VIEW;
        }
        return path;
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }
    public String getMethod() {
        return method;
    }
    public String getUrl(){
        return url;
    }
    public String getPath() {
        return path;
    }
    public String getHttpVersion() {
        return httpVersion;
    }

    public String getContentType(){
        String pathURL = getViewPath();
        int index = pathURL.lastIndexOf(DOT);

        String extension = pathURL.substring(index+1);
        return switch(extension){
            case "html" -> "text/html;charset=utf-8";
            case "css" -> "text/css";
            case "js" -> "application/javascript";
            case "ico" -> "image/x-icon";
            case "png" -> "image/png";
            case "jpg", "jpeg" -> "image/jpeg";
            case "svg" -> "image/svg+xml";
            default -> throw new IllegalStateException("Unexpected value: " + extension);
        };
    }

    public byte[] getBody() {
        return body;
    }
    public void setBody(byte[] body) {
        this.body = body;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder(LF)
                .append(method).append(BLANK)
                .append(url).append(BLANK)
                .append(httpVersion).append(LF);

        for (String key : headers.keySet()) {
            sb.append(key).append(COLON + BLANK)
                    .append(headers.get(key)).append(LF);
        }

        return sb.toString();
    }

}
