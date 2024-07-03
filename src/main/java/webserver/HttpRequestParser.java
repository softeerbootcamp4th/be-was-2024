package webserver;

public class HttpRequestParser {
    private static final HttpRequestParser instance = new HttpRequestParser();

    public static HttpRequestParser getInstance() {
        return instance;
    }

    public String parseRequestURI(String requestLine) {
        // Split the request line by spaces
        String[] tokens = requestLine.split("\\s+");

        // The request URI (path) is the second element in the tokens array
        if (tokens.length >= 2) {
            return tokens[1];
        } else {
            throw new IllegalArgumentException("Invalid HTTP request line: " + requestLine);
        }
    }

    public String parseRequestContentType(String requestLine) {
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
