package webserver.enums;

public enum ContentType {
    HTML("text/html"),
    CSS("text/css"),
    JS("application/javascript"),
    ICO("image/x-icon"),
    PNG("image/png"),
    JPG("image/jpeg"),
    SVG("image/svg+xml");

    private final String contentType;

    private ContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }
}
