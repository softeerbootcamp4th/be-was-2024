package webserver;

public class URLParser {
    public boolean parseURL(String line) {
        String[] splitedURL = line.split(" ");
        if (splitedURL[0].equals("GET")) {
            System.out.println(splitedURL[0]);
        }
        return false;
    }

    public String getGetURL(String line) {
        String[] splitedURL = line.split(" ");
        String resource = splitedURL[1];
        if (resource.equals("/index.html")) {
            return resource;
        }
        return splitedURL[1];
    }

    public String getHttpMethod(String line) {
        String[] splitedURL = line.split(" ");
        return splitedURL[0];
    }
}
