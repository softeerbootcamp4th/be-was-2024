package util;

public enum ContentType {
    // https://developer.mozilla.org/ko/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types
    HTML("html", "text/html;charset=utf-8"),
    CSS("css", "text/css"),
    JS("js", "application/javascript"),
    ICO("ico", "image/vnd.microsoft.icon"),
    PNG("png", "image/png"),
    JPG("jpg", "image/jpeg"),
    JPEG("jpeg", "image/jpeg"),
    SVG("svg", "image/svg+xml");

    private final String extension;
    private final String type;

    ContentType(String extension, String contentType) {
        this.extension = extension;
        this.type = contentType;
    }

    public static String getType(String extension) {
        for (ContentType contentType : values()) {
            if (contentType.extension.equals(extension)) {
                return contentType.type;
            }
        }
        return null;
    }

    public String getExtension() {
        return extension;
    }
}
