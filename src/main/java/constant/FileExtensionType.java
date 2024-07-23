package constant;

/**
 * File 확장자 타입 Enum
 */
public enum FileExtensionType {
    NULL(""),
    HTML("text/html"),
    PLAIN("text/plain"),
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

    public static FileExtensionType of(String contentType) {
        for (FileExtensionType fileType : values()) {
            if (fileType.getContentType().equals(contentType)) {
                return fileType;
            }
        }
        return null;
    }
}
