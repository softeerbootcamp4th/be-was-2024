package webserver;

public class URLParser {
    public String getGetURL(String line) {
        return line.split(" ")[1];
    }

    public String getHttpMethod(String line) {
        return line.split(" ")[0];
    }
}
