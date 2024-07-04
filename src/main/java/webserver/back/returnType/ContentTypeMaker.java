package webserver.back.returnType;
public class ContentTypeMaker {
    public static String getContentType(String extension) {
        System.out.println(BaseContentType.getBaseContentType(extension)+"/"+ContentType.getContentType(extension));
        return BaseContentType.getBaseContentType(extension)+"/"+ContentType.getContentType(extension);
    }
}
