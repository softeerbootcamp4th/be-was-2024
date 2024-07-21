package constant;

/**
 * File 확장자 타입 Enum
 */
public enum FileExtensionType {
    HTML("text/html"),
    CSS("text/css"),
    JS("application/javascript"),
    ICO("image/x-icon"),
    PNG("image/png"),
    JPG("image/jpeg"),
    SVG("image/svg+xml");

    final String contentType;

    FileExtensionType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }
}
