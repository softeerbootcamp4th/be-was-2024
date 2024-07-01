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
}
