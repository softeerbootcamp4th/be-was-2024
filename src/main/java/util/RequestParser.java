package util;

import model.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestParser {
    private static final Logger log = LoggerFactory.getLogger(RequestParser.class);

    /**
     * HTTP request에서 확장자를 분리하는 로직
     * Request url을 .으로 분리 한뒤, 마지막 리스트 값을 가져옴.
     * https://developer.mozilla.org/ko/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types
     */
    public static String parseContentTypeFromRequestPath(String httpRequestPath) {
        String[] splitPath = httpRequestPath.split("\\.");
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

    /**
     * InputStream을 HttpRequest 객체로 변환하는 로직
     */
    public static HttpRequest convertInputStreamToHttpRequest(InputStream in) throws IOException {
        BufferedReader buffer = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line = buffer.readLine();
        String httpMethod = line.split(" ")[0];
        String path = line.split(" ")[1];
        String protocolVersion = line.split(" ")[2];

        List<String> headers = new ArrayList<>();

        while (!line.isEmpty()) {
            line=buffer.readLine();
            headers.add(line);
        }

        return new HttpRequest(httpMethod, path, protocolVersion, headers);

    }


}
