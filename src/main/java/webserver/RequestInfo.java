package webserver;

import java.util.Arrays;

enum MethodType {
    GET("GET"),
    POST("POST");

    private final String value;

    MethodType(String value) { this.value = value; }

    public String getValue() { return value; }
}

enum MIMEType {
    HTML("html", "text/html"),
    CSS("css", "text/css"),
    JS("js", "application/javascript"),
    ICO("ico", "image/x-icon"),
    PNG("png", "image/png"),
    JPG("jpg", "image/jpeg"),
    SVG("svg", "image/svg+xml"),
    DEFAULT("", "text/plain");

    private final String value;
    private final String contentType;

    MIMEType(String value, String contentType) {
        this.value = value;
        this.contentType = contentType;
    }

    public static MIMEType findByContentType(String contentType) {
        return Arrays.stream(MIMEType.values())
                .filter(type -> type.getValue().equals(contentType.toLowerCase()))
                .findAny()
                .orElse(DEFAULT);
    }

    public String getValue() { return value; }

    public String getContentType() { return contentType; }
}

public class RequestInfo {
    private MethodType method;
    private MIMEType mime;
    private String path;

    public RequestInfo(String requestLine) {
        method = findMethod(requestLine);
        path = findPath(requestLine);
        if (path.equals("/")) {
            path = "/index.html";
        }
        mime = findMIME(path);
    }

    private static MethodType findMethod(String requestLine) {
        return MethodType.valueOf(requestLine.split(" ")[0]);
    }

    private static String findPath(String requestLine) {
        return requestLine.split(" ")[1];
    }

    private static MIMEType findMIME(String path) {
        String[] list = path.split("\\.");
        return MIMEType.findByContentType(list[list.length - 1]);
    }

    public String getMethod() { return method.getValue(); }

    public String getContentType() { return mime.getContentType(); }

    public String getPath() { return path; }
}