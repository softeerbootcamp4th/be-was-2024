package returnType;

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
        for(BaseContentType type : BaseContentType.values()) {
            for(String ext : type.extensions) {
                if(extension.endsWith(ext)) {
                    return type.contentType;
                }
            }
        }
        return TEXT.contentType;
        //없다면 text/plain
    }
}
