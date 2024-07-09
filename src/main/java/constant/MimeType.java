package constant;


public enum MimeType{
    APPLICATON_JSON("application/json"),
    APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded");

    private final String typeName;

    MimeType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public static MimeType findByTypeName(String typeName) {
        for (MimeType mimeType : values()) {
            if (mimeType.getTypeName().equals(typeName)) {
                return mimeType;
            }
        }
        return null;
    }
}
