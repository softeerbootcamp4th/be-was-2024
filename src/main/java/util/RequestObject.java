package util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class RequestObject {

    // GET /user/create?name="1234"&password="1" 이면

    private String path;//  /user/create 가 들어옴
    private final String method;// GET이 들어옴
    private Map<String,String> params; // { name : 1234 , password : 1} 들어옴

    public RequestObject(String line)
    {
        this.method=line.split(" ")[0];
        this.path = line.split(" ")[1];
        getQueryParams();
    }
    public String getPath()
    {
        return this.path;
    }

    public String getMethod()
    {
        return this.method;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void getQueryParams()
    {
        if (path == null || path.isEmpty()) {
            return ;
        }
        int idx = path.indexOf('?');
        if (idx < 0) {
            return ;
        }
        String queryString = path.substring(idx + 1);
        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                params.put(keyValue[0], keyValue[1]);
            }
        }
    }
}
