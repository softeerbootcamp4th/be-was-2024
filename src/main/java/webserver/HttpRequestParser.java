package webserver;

public class HttpRequestParser {

    public static String parseRequestURI(String requestLine) {
        // Split the request line by spaces
        String[] tokens = requestLine.split("\\s+");

        // The request URI (path) is the second element in the tokens array
        if (tokens.length >= 2) {
            return tokens[1];
        } else {
            throw new IllegalArgumentException("Invalid HTTP request line: " + requestLine);
        }
    }

    public static String parseRequestContentType(String requestLine) {
        String[] pathParts = requestLine.split("/");
        if (pathParts.length < 1) {
            return null;
        }
        String lastPathPart = pathParts[pathParts.length - 1];

        // 파일 이름에서 확장자 추출
        int lastDotIndex = lastPathPart.lastIndexOf('.');
        if (lastDotIndex != -1) {
            return lastPathPart.substring(lastDotIndex + 1);
        } else {
            return null;
        }
    }
}
