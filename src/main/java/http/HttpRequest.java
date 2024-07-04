package http;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class HttpRequest {
    private final Map<String, String> headers = new TreeMap<>();

    private final String defaultViewPath = "/index.html";

    private final String method;
    private final String url;
    private final String version;

    public HttpRequest(String method, String url, String version) {
        this.method = method;
        this.url = url;
        this.version = version;
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
    public String getVersion() {
        return version;
    }
    public String getPathURL() {
        int index = url.lastIndexOf('.');
        if(index == -1){
            return url + defaultViewPath;
        }
        return url;
    }

    private String setURL(String urlString){
        int index = urlString.lastIndexOf('.');
        if(index == -1){
            urlString += "/index.html";
        }

        return urlString;
    }

    public String getContentType(){
        String pathURL = getPathURL();
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

        /*for (String key : headers.keySet()) {
            sb.append(key).append(": ").append(headers.get(key)).append("\n");
        }*/
        for(Map.Entry<String, String> entry : headers.entrySet()){
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        return sb.toString();
    }

}
