package util;

/**
 * MIME 타입을 정의한 enum 클래스
 */
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

    /**
     * 확장자에 해당하는 MIME 타입을 반환하는 메서드
     * @param extension
     * @return String
     */
    public static String getType(String extension) {
        for (ContentType contentType : values()) {
            if (contentType.extension.equals(extension)) {
                return contentType.type;
            }
        }
        return "text/plain;charset=utf-8";
    }

    /**
     * 지원하는 확장자인지 확인하는 메서드
     * @param extension
     * @return boolean
     */
    public static boolean isSupported(String extension) {
        for (ContentType contentType : values()) {
            if (contentType.extension.equals(extension)) {
                return true;
            }
        }
        return false;
    }

    public String getExtension() {
        return extension;
    }
}
