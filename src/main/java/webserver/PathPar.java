package webserver;

public class PathPar {

    public String getUrl(String line) {
        return line.split(" ")[1];
    }
    public String getHttpMethod(String line)
    {
        return line.split(" ")[0];
    }
}
