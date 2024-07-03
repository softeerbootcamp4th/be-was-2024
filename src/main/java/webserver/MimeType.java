package webserver;

public enum MimeType {
    HTML("text/html"),
    CSS("text/css"),
    JAVASCRIPT("text/javascript"),
    ICO("image/vnd.microsoft.icon"),
    PNG("image/png"),
    JPG("image/jpeg"),
    SVG("image/svg+xml");

    private String mimeType;

    MimeType(String mimeType) {
       this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }


    @Override
    public String toString() {
        return getMimeType();
    }
}
