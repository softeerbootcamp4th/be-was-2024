package webserver.back.returnType;

import java.util.Arrays;

public enum BaseContentType {
    TEXT(new String[]{"html", "css"},"text"),
    IMAGE(new String[]{"png", "jpg", "ico", "svg"},"image"),
    APPLICATION(new String[]{"json"},"application");

    private final String[] extensions;
    private final String contentType;

    BaseContentType(String[] extensions, String contentType) {
        this.extensions = extensions;
        this.contentType = contentType;
    }
    public static String getBaseContentType(String extension) {
        return Arrays.stream(BaseContentType.values())
                .filter(type -> Arrays.stream(type.extensions).anyMatch(ext -> extension.endsWith(ext)))
                .findAny()
                .map(type -> type.contentType)
                .orElse(TEXT.contentType);
        //없다면 text/plain
    }
}
