package util;

import java.io.*;
import java.util.Map;
import java.util.HashMap;

public class RequestLine {


    private String path;
    private final String method;


    public RequestLine(String line)
    {
        this.method=line.split(" ")[0];
        this.path = "src/main/resources/static"+line.split(" ")[1];
        File fi = new File(path);
        this.path = fi.isDirectory()? this.path+"/index.html" : this.path;
    }
    public String getPath()
    {
        return this.path;
    }

    public String getMethod()
    {
        return this.method;
    }

    /*public Map<String, String> getQueryParams(String requestLine) {
        Map<String, String> queryParams = new HashMap<>();
        if (requestLine == null || requestLine.isEmpty()) {
            return queryParams;
        }
        String url = getUrl(requestLine);
        int idx = url.indexOf('?');
        if (idx < 0) {
            return queryParams;
        }
        String queryString = url.substring(idx + 1);
        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                queryParams.put(keyValue[0], keyValue[1]);
            }
        }
        return queryParams;
    }
     */
}
