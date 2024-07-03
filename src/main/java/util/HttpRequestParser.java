package util;


public class HttpRequestParser {

    // to prevent instantiation
    private HttpRequestParser() {
    }

    public static String[] parseRequestLine(String requestLine){
        // 0: RequestMethod (ex: GET)
        // 1: RequestURI (ex: /index.html)
        // 2: HTTP version (ex: HTTP/1.1
        return requestLine.split(" ");
    }

    public static String parseRequestMethod(String requestLine){
        return requestLine.split(" ")[0];
    }

    public static String parseRequestURI(String requestLine){
        return requestLine.split(" ")[1];
    }
}
