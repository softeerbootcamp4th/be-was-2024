package enums;

import java.util.HashMap;
import java.util.Map;

public enum FileType {
    html("html", "text/html"),
    css("css", ""),
    js("js", "Application/javascript"),
    ico("ico", "image/x-icon"),
    png("png", "image/png"),
    jpg("jpg", "image/jpeg"),
    svg("svg", "image/svg+xml")
    ;

    private static final Map<String, String> extensionMap = new HashMap<>();

    static {
        for (FileType fileType : values()) {
            extensionMap.put(fileType.extension, fileType.contentType);
        }
    }

    private final String extension;
    private final String contentType;

    FileType(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }

    public static String getContentTypeByExtension(String extension) {
        return extensionMap.get(extension);
    }
}
