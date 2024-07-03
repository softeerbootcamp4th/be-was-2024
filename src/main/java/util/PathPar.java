package util;

import java.io.*;


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
}
