package util;

public class ExtensionMapper {
    /**
     * HTTP request의 path를 이용하여 확장자를 알아내는 로직
     * Request path를 .으로 분리 한뒤, 마지막 리스트 값을 가져옴.
     * https://developer.mozilla.org/ko/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types
     *
     * @return : HttpRequest에 대한 응답헤더의 Content-type
     */
    public static String getContentTypeFromRequestPath(String requestPath  ) {
        String[] splitPath = requestPath.split("\\.");
        if (splitPath.length == 0) {
            return "text/plain";
        }
        String extension = splitPath[splitPath.length - 1];

        return switch (extension) {
            case "html" -> "text/html";
            case "svg" -> "image/svg+xml";
            case "jpg" -> "image/jpeg";
            case "js" -> "application/javascript";
            case "css" -> "text/css";
            case "gif" -> "image/gif";
            case "ico" -> "image/vnd.microsoft.icon";
            default -> "text/plain";
        };
    }
}
