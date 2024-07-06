package webserver.enumPackage;

import java.util.HashMap;
import java.util.Map;

public enum ContentType {
    HTML("html", "text/html"),
    CSS("css", "text/css"),
    JAVASCRIPT("js", "application/javascript"),
    JSON("json", "application/json"),
    PNG("png", "image/png"),
    JPEG("jpg", "image/jpeg"),
    JPEG2("jpeg", "image/jpeg"),
    SVG("svg", "image/svg+xml"),
    ICO("ico", "image/x-icon"),
    OCTET_STREAM("octet-stream", "application/octet-stream");

    private final String extension;
    private final String mimeType;
    private static final Map<String, ContentType> extensionToContentTypeMap = new HashMap<>();

    static {
        for (ContentType contentType : values()) {
            extensionToContentTypeMap.put(contentType.getExtension(), contentType);
        }
    }

    ContentType(String extension, String mimeType) {
        this.extension = extension;
        this.mimeType = mimeType;
    }

    public String getExtension() {
        return extension;
    }

    public String getMimeType() {
        return mimeType;
    }

    public static String fromExtension(String extension) {
        return extensionToContentTypeMap.getOrDefault(extension.toLowerCase(), OCTET_STREAM).getMimeType();
    }
}