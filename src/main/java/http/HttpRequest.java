package http;

import exception.QueryParameterNotFoundException;

import java.util.Map;
import java.util.TreeMap;

public class HttpRequest {
    private final Map<String, String> queryParameters = new TreeMap<>();
    private final Map<String, String> headers = new TreeMap<>();

    private final String defaultViewPath = "/index.html";

    private final String method;
    private final String url;
    private final String path;
    private final String version;


    public HttpRequest(String method, String url, String version) {
        this.method = method;
        this.url = url;
        this.version = version;
        if(hasQueryParameters(url)) {
            setQueryParameters(url);
            this.path = url.substring(0, url.indexOf('?'));
        }
        else {
            this.path = url;
        }
    }

    //Map<> queryParameters를 외부에서 접근 불가능하게 설정했으므로 getter 작성
    public String getQueryParameterValue(String key) throws QueryParameterNotFoundException {
        if(queryParameters.containsKey(key)) {
            return queryParameters.get(key);
        } else throw new QueryParameterNotFoundException("Cannot find Query Parameter names :  " + key);
    }

    /*
    public String getQueryParameter() {
        StringBuilder queries = new StringBuilder();
        for (String key : queryParameters.keySet()) {
            queries.append(key).append("=").append(queryParameters.get(key)).append("\n");
        }
        return queries.toString();
    }*/

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
    public String getVersion() {
        return version;
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
                .append(version).append("\n");

        for(Map.Entry<String, String> entry : headers.entrySet()){
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        return sb.toString();
    }

}
