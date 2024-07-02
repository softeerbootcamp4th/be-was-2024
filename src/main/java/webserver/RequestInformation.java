package webserver;

enum MethodType {
    GET("GET"),
    POST("POST");

    private String value;

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
    SVG("svg", "image/svg+xml");

    private String value;
    private String contentType;

    MIMEType(String value, String contentType) {
        this.value = value;
        this.contentType = contentType;
    }

    public String getValue() { return value; }

    public String getContentType() { return contentType; }
}

public class RequestInformation {
    private MethodType method;
    private MIMEType mime;
    private String path;

    public RequestInformation(String requestLine) {
        method = findMethod(requestLine);
        path = findPath(requestLine);
        if (path.equals("/")) {
            path = "/index.html";
        }
        mime = findMime(path);
    }

    private static MethodType findMethod(String requestLine) {
        return MethodType.valueOf(requestLine.split(" ")[0]);
    }

    private static String findPath(String requestLine) {
        return requestLine.split(" ")[1];
    }

    private static MIMEType findMime(String path) {
        String[] list = path.split("\\.");
        return MIMEType.valueOf(list[list.length - 1].toUpperCase());
    }

    public String getMethod() { return method.getValue(); }

    public String getContentType() { return mime.getContentType(); }

    public String getPath() { return path; }
}