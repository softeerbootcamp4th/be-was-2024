package util;

import java.util.HashMap;
import java.util.Map;

public class RequestObject {

    // GET /user/create?name="1234"&password="1" 이면

    private String path;//  /user/create 가 들어옴
    private final String method;// GET이 들어옴
    private String paramLine;
    private Map<String,String> params = new HashMap<String,String>(); // { name : 1234 , password : 1} 들어옴

    public RequestObject(String line)
    {
        String[] requestLine = line.split(" ");
        this.method=requestLine[0];
        String[] url = requestLine[1].split("\\?");
        this.path = url[0];
        if(url.length>1)
        {
            this.paramLine = url[1];
            getQueryParams();
            System.out.println(this.paramLine);
        }

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

        return this.params;
    }

    public void getQueryParams()
    {
        if (paramLine == null || paramLine.isEmpty()) {
            return ;
        }
        String[] pairs = paramLine.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                params.put(keyValue[0], keyValue[1]);
            }
        }
    }
}
