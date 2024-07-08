package util;

import java.util.HashMap;
import java.util.Map;

public class RequestObject {

    // GET /user/create?name="1234"&password="1" HTTP/1.1 이면

    private String path;//  /user/create 가 들어옴
    private final String method;// GET이 들어옴
    private final String version;//HTTP version

    public RequestObject(String line)
    {
        String[] requestLine = line.split(" ");
        this.method=requestLine[0];
        this.path = requestLine[1];
        this.version=requestLine[2];
    }
    public String getPath()
    {
        return this.path;
    }

    public String getMethod()
    {
        return this.method;
    }

    public String getVersion()
    {
        return this.version;
    }

}
