package constant;

/**
 * HttpRequest의 Mime Type Enum
 */
public enum MimeType{
    NULL(""),
    APPLICATON_JSON("application/json"),
    APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded"),
    MULTIPART_FORM_DATA("multipart/form-data");

    private final String typeName;

    MimeType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public static MimeType of(String typeName) {
        for (MimeType mimeType : values()) {
            if (mimeType.getTypeName().equals(typeName)) {
                return mimeType;
            }
        }
        return null;
    }
}
