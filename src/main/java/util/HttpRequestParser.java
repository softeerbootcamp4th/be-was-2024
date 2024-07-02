package util;


public class HttpRequestParser {

    public static String[] parseRequestLine(String requestLine){
        // 0: RequestType (ex: GET)
        // 1: RequestURI (ex: /index.html)
        // 2: HTTP version (ex: HTTP/1.1
        return requestLine.split(" ");
    }
}
