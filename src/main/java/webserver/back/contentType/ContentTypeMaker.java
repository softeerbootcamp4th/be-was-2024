package webserver.back.contentType;
public class ContentTypeMaker {
    public static String getContentType(String extension) {
        return BaseContentType.getBaseContentType(extension)+"/"+ContentType.getContentType(extension);
    }
}
