package returnType;

import java.util.stream.Stream;

public enum ContentType {
    HTML("html","html"),
    CSS("css","css"),
    JSON("json","json"),
    PLAIN("plain","plain"),
    PNG("png","png"),
    JPG("jpg","jpeg"),
    ICO("ico","x-icon"),
    SVG("svg","svg+xml"),
    ;

    private String extension;
    private String contentType;

    ContentType(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }
    public static String getContentType(String extension){
        return Stream.of(ContentType.values())
                .filter(ct -> extension.endsWith(ct.extension))
                .map(ct->ct.contentType)
                .findFirst()
                .orElse(PLAIN.contentType);
    }
}
