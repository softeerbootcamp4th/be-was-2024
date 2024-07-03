package util;

public class RequestParser {
    /**
     * HTTP request에서 요총 path를 분리하는 로직
     */
    public static String parseUriFromRequestHeaderStartLine(String requestHeaderStartLine) {
        return requestHeaderStartLine.split(" ")[1];
    }

    /**
     * HTTP request에서 확장자를 분리하는 로직
     * Request url을 .으로 분리 한뒤, 마지막 리스트 값을 가져옴.
     * https://developer.mozilla.org/ko/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types
     */
    public static String parseContentTypeFromRequestHeaderStartLine(String requestHeaderStartLine) {
        String path = parseUriFromRequestHeaderStartLine(requestHeaderStartLine);
        String[] splitPath = path.split("\\.");
        if (splitPath.length == 0) {
            return "text/plain";
        }
        String extension = splitPath[splitPath.length - 1];

        return switch (extension) {
            case "html" -> "text/html";
            case "svg" -> "image/svg+xml";
            case "jpg" -> "image/jpeg";
            case "js" -> "application/javascript";
            case "css" -> "test/css";
            case "gif" -> "image/gif";
            case "ico" -> "image/vnd.microsoft.icon";
            default -> "text/plain";
        };
    }


}
