package web;

public enum ContentType {
    HTML("html", "text/html"),
    CSS("css", "text/css"),
    JS("js", "application/javascript"),
    ICO("ico", "image/x-icon"),
    PNG("png", "image/png"),
    JPG("jpg", "image/jpeg"),
    SVG("svg", "image/svg+xml"),
    UNKNOWN("default", "*/*");

    private final String key;
    private final String type;

    ContentType(String key, String type) {
        this.key = key;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public String getType() {
        return type;
    }

    public static ContentType findByKey(String key) {
        for (ContentType contentType : ContentType.values()) {
            if (contentType.key.equals(key)) {
                return contentType;
            }
        }
        return ContentType.UNKNOWN;
    }
}
