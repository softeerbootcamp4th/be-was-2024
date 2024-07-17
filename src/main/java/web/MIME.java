package web;

public enum MIME {
    HTML("html", "text/html"),
    CSS("css", "text/css"),
    JS("js", "application/javascript"),
    ICO("ico", "image/x-icon"),
    PNG("png", "image/png"),
    JPG("jpg", "image/jpeg"),
    SVG("svg", "image/svg+xml"),
    JSON("json", "application/json"),
    FORM("form", "application/x-www-form-urlencoded"),
    MULTIPART("multipart", "multipart/form-data"),
    UNKNOWN("default", "*/*");

    private final String key;
    private final String type;

    MIME(String key, String type) {
        this.key = key;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public String getType() {
        return type;
    }

    public static MIME findByKey(String key) {
        for (MIME mime : MIME.values()) {
            if (mime.key.equals(key)) {
                return mime;
            }
        }
        return MIME.UNKNOWN;
    }
}
