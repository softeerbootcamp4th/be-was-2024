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

}
