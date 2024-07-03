package util;

public class HttpRequestObject {

    private final String requestMethod;
    private final String requestURI;
    private final String httpVersion;

    private HttpRequestObject(String requestMethod, String requestURI, String httpVersion){
        this.requestMethod = requestMethod;
        this.requestURI = requestURI;
        this.httpVersion = httpVersion;
    }

    public static HttpRequestObject from(String requestLine) {
        String[] requestLineElements = requestLine.split(" ");
        return new HttpRequestObject(requestLineElements[0], requestLineElements[1], requestLineElements[2]);
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getHttpVersion() {
        return httpVersion;
    }
}
