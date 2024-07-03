package util;

import java.io.*;
import java.util.Map;
import java.util.HashMap;

public class PathPar {

    public String getUrl(String line)
    {
        return line.split(" ")[1];
    }

    public String getHttpMethod(String line)
    {
        return line.split(" ")[0];
    }

    public String getDir(String line)
    {
        File fi = new File(line);
        return fi.isDirectory() ? line+"/index.html" : line;
    }

    public Map<String, String> getQueryParams(String requestLine) {
        Map<String, String> queryParams = new HashMap<>();
        if (requestLine == null || requestLine.isEmpty()) {
            return queryParams;
        }
        String[] parts = requestLine.split(" ");
        if (parts.length < 2) {
            return queryParams;
        }
        String url = parts[1];
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
}
