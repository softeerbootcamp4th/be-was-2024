package http.enums;

public enum MIMEType {
    html("text/html"),
    css("text/css"),
    js("text/javascript"),
    ico("image/ico"),
    png("image/png"),
    jpg("image/jpg"),
    svg("image/svg+xml");

    private final String mimeType;

    MIMEType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }
}
