package enums;

public enum Type {
    HTML("html", "text/html"),
    CSS("css", "text/css"),
    JS("js", "text/javascript"),
    JSON("json", "application/json"),
    ICO("ico", "image/x-icon"),
    PNG("png", "image/png"),
    SVG("svg", "image/svg+xml"),
    JPG("jpg", "image/jpeg");

    private final String type;
    private final String mime;

    Type(String type, String mime) {
        this.type = type;
        this.mime = mime;
    }

    public String getType() {
        return this.type;
    }

    public String getMime() {
        return this.mime;
    }
}
