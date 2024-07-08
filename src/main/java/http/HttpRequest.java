package http;

import exception.QueryParameterNotFoundException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;

public class HttpRequest {
    private final Map<String, String> queryParameters = new TreeMap<>();
    private final Map<String, String> headers = new TreeMap<>();

    private final String defaultViewPath = "/index.html";

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
            this.path = url.substring(0, url.indexOf('?'));
        }
        else {
            this.path = url;
        }
    }
    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public String getQueryParameterValue(String key) throws QueryParameterNotFoundException {
        if(queryParameters.containsKey(key)) {
            return queryParameters.get(key);
        } else throw new QueryParameterNotFoundException("Cannot find Query Parameter names :  " + key);
    }

    private boolean hasQueryParameters(String url){
        return url != null && url.contains("?");
    }

    private void setQueryParameters(String url){
        String query = url.substring(url.indexOf("?") + 1);
        String[] params = query.split("&");
        for(String param : params){
            String[] keyValue = param.split("=");
            queryParameters.put(keyValue[0], keyValue[1]);
        }
    }

    private boolean isRootPath(String path){
        return path.equals("/");
    }

    public String getViewPath() {
        int index = path.lastIndexOf('.');
        if(index == -1){
            if(isRootPath(path)) return defaultViewPath;
            else return path + defaultViewPath;
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
        int index = pathURL.lastIndexOf('.');

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

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("\n")
                .append(method).append(" ")
                .append(url).append(" ")
                .append(httpVersion).append("\n");

        for(Map.Entry<String, String> entry : headers.entrySet()){
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        return sb.toString();
    }

}
