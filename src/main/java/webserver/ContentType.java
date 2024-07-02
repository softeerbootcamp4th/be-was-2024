package webserver;

public enum ContentType {
    html("text/html"),
    css("text/css"),
    js("application/javascript"),
    ico("image/x-icon"),
    png("image/png"),
    jpg("image/jpeg"),
    svg("image/svg+xml");

    private String contentType;

    private ContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }
}
