package type;

import java.util.Arrays;

public enum MIME {
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

    MIME(String value, String contentType) {
        this.value = value;
        this.contentType = contentType;
    }

    public static MIME findByContentType(String contentType) {
        return Arrays.stream(MIME.values())
                .filter(type -> type.getValue().equals(contentType.toLowerCase()))
                .findAny()
                .orElse(DEFAULT);
    }

    public String getValue() { return value; }

    public String getContentType() { return contentType; }
}